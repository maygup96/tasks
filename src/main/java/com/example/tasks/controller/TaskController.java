package com.example.tasks.controller;

import com.example.tasks.entity.Data;
import com.example.tasks.entity.StatusData;
import com.example.tasks.repository.*;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.example.tasks.dto.TaskCountResponse;
import com.example.tasks.repository.GardeningRepository;
import com.example.tasks.repository.LaundaryRepository;
import com.example.tasks.repository.ReadingRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {

    @Value("${aws.sqs.queue-url}")
    private String queueUrl;

    private final SqsClient sqsClient;
    private final GardeningRepository gardeningRepository;
    private final LaundaryRepository laundryRepository;
    private final ReadingRepository readingRepository;
    private final DataRepository dataRepository;

    // Fixed thread pool limited to at most 2 concurrent execution loops
    private final ExecutorService threadPool = Executors.newFixedThreadPool(2);
    private volatile boolean isRunning = true;

    public TaskController(SqsClient sqsClient, GardeningRepository gardeningRepository,
            LaundaryRepository laundryRepository, ReadingRepository readingRepository,
            DataRepository dataRepository) {
		this.sqsClient = sqsClient;
		this.gardeningRepository = gardeningRepository;
		this.laundryRepository = laundryRepository;
		this.readingRepository = readingRepository;
		this.dataRepository = dataRepository;
	}

    @GetMapping("/counts")
    public TaskCountResponse getAllTaskCounts() {
        // Fetch the native row counts concurrently across tables
        long gardeningCount = gardeningRepository.count();
        long laundryCount = laundryRepository.count();
        long readingCount = readingRepository.count();

        // Return packaged statistics payload DTO
        return new TaskCountResponse(gardeningCount, laundryCount, readingCount);
    }
    
    @GetMapping("/trigger/{count}")
    public String triggerSqsEvents(@PathVariable int count) {
        String[] targetedTables = {"laundry", "gardening", "reading"};

        for (String table : targetedTables) {
            // Build message string format: {"table": "laundry", "requestedCount": 5}
            String messageBody = String.format("{\"table\": \"%s\", \"requestedCount\": %d}", table, count);

            SendMessageRequest sendMsgRequest = SendMessageRequest.builder()
                    .queueUrl(queueUrl)
                    .messageBody(messageBody)
                    .build();

            sqsClient.sendMessage(sendMsgRequest);
        }
        return "Dispatched 3 task aggregation messages safely onto AWS SQS queue!";
    }
    
    // Automatically activates SQS listeners inside our thread pool on start
    @PostConstruct
    public void initPolling() {
        for (int i = 0; i < 2; i++) {
            threadPool.submit(this::pollAndProcessSqs);
        }
    }

    // Continuous processing background routine executed by the 2 threads
    private void pollAndProcessSqs() {
        while (isRunning) {
            try {
                ReceiveMessageRequest receiveRequest = ReceiveMessageRequest.builder()
                        .queueUrl(queueUrl)
                        .maxNumberOfMessages(1)
                        .waitTimeSeconds(10) // Long-polling saves execution costs
                        .build();

                List<Message> messages = sqsClient.receiveMessage(receiveRequest).messages();

                for (Message message : messages) {
                    processMessagePayload(message);

                    // Deletes message out of SQS once calculation completes successfully
                    DeleteMessageRequest deleteRequest = DeleteMessageRequest.builder()
                            .queueUrl(queueUrl)
                            .receiptHandle(message.receiptHandle())
                            .build();
                    sqsClient.deleteMessage(deleteRequest);
                }
            } catch (Exception e) {
                System.err.println("Worker thread error polling SQS: " + e.getMessage());
            }
        }
    }

    private void processMessagePayload(Message message) {
        String body = message.body();
        System.out.println("Thread processing payload from AWS SQS: " + body);

        // Simple manual string extractions safely replacing structural JSON library dependencies
        String targetTable = "";
        if (body.contains("laundry")) targetTable = "laundry";
        else if (body.contains("gardening")) targetTable = "gardening";
        else if (body.contains("reading")) targetTable = "reading";

        long actualCalculatedCount = 0;
        if ("laundry".equals(targetTable)) actualCalculatedCount = laundryRepository.count();
        else if ("gardening".equals(targetTable)) actualCalculatedCount = gardeningRepository.count();
        else if ("reading".equals(targetTable)) actualCalculatedCount = readingRepository.count();

        // Persist records back into tracking tables
        StatusData trackingStatus = new StatusData();
        trackingStatus.setUser_name("SystemWorkerPool");
        trackingStatus.setStatus("DONE");

        Data dataAuditLog = new Data();
        dataAuditLog.setTabName(targetTable);
        dataAuditLog.setCount((int)actualCalculatedCount);
        dataAuditLog.setStatus("DONE");

        dataRepository.save(dataAuditLog);
        System.out.println("Successfully calculated and set status to DONE for table: " + targetTable);
    }
    
    @PreDestroy
    public void cleanUpExecutor() {
        isRunning = false;
        threadPool.shutdownNow();
    }

}

   

