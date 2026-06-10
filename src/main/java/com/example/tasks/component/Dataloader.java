package com.example.tasks.component;

import com.example.tasks.entity.Data;
import com.example.tasks.entity.StatusData;
import com.example.tasks.repository.DataRepository;
import com.example.tasks.repository.GardeningRepository;
import com.example.tasks.repository.LaundaryRepository;
import com.example.tasks.repository.ReadingRepository;
import com.example.tasks.repository.StatusDataRepository;
import com.example.tasks.entity.Gardening;
import com.example.tasks.entity.Reading;
import com.example.tasks.entity.Laundary;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class Dataloader implements CommandLineRunner {

    private final DataRepository dataRepository;
    private final StatusDataRepository statusDataRepository;
	private final ReadingRepository readingRepository;
	private final LaundaryRepository laundaryRepository;
	private final GardeningRepository gardeningRepository;

    // Spring automatically injects both repositories here
    public Dataloader(
    		DataRepository dataRepository,
    		StatusDataRepository statusDataRepository,
    		GardeningRepository gardeningRepository,
    		LaundaryRepository laundaryRepository,
    		ReadingRepository readingRepository
    ) {
        this.dataRepository = dataRepository;
        this.statusDataRepository = statusDataRepository;
        this.gardeningRepository = gardeningRepository;
        this.laundaryRepository = laundaryRepository;
        this.readingRepository = readingRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Check if the data table is completely empty
        if (dataRepository.count() == 0) {
            System.out.println("RDS tables are empty. Seeding initial data...");

            // Step 1: Create and save the parent StatusData record first
            StatusData parentStatus = new StatusData();
            parentStatus.setStatus("Active");
            parentStatus.setUser_name("admin_user");
            // Save to RDS to generate its primary key ID
            parentStatus = statusDataRepository.save(parentStatus); 

//            // Step 2: Create the child Data record and link the parent
//            Data initialData = new Data();
//            initialData.setCount(0);
//            initialData.setTab_index(0);
//            initialData.setStatus("Processing");
//            initialData.setParent_status(parentStatus); // Pass the saved parent object here
//            dataRepository.save(initialData);
//            
//            initialData = new Data();
//            initialData.setCount(0);
//            initialData.setTab_index(1);
//            initialData.setStatus("Processing");
//            initialData.setParent_status(parentStatus); // Pass the saved parent object here
//            dataRepository.save(initialData);
//            
//            initialData = new Data();
//            initialData.setCount(0);
//            initialData.setTab_index(2);
//            initialData.setStatus("Processing");
//            initialData.setParent_status(parentStatus); // Pass the saved parent object here
//            dataRepository.save(initialData);
            
            Reading reading = new Reading();
            reading.setArticle_type("blog");
            reading.setName("security");
            reading.setPages(7);
            reading = readingRepository.save(reading);
            
            Gardening gardenData = new Gardening();
            gardenData.setActivity_type("seed plant");
            gardenData.setPlant_type("sapling");
            gardenData.setReferences(reading);
            gardeningRepository.save(gardenData);
            
            reading = new Reading();
            reading.setArticle_type("blog");
            reading.setName("CTF");
            reading.setPages(65);
            reading = readingRepository.save(reading);
            
            gardenData = new Gardening();
            gardenData.setActivity_type("seed plant");
            gardenData.setPlant_type("sapling");
            gardenData.setReferences(reading);
            gardeningRepository.save(gardenData);
            
            Laundary laundary = new Laundary();
            laundary.setColor("white");
            laundary.setFabric("cotton");
            laundary.setGarment_type("t-shirt");
            laundaryRepository.save(laundary);
            
            System.out.println("Initial records successfully loaded into RDS PostgreSQL!");
        } else {
            System.out.println("Data already exists in RDS. Skipping initialization.");
        }
    }
}
