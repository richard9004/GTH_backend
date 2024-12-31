package com.cars.carbookings.services.organizer;

import com.cars.carbookings.dto.OrganizerRequest;
import com.cars.carbookings.entities.Organizer;
import com.cars.carbookings.repositories.OrganizerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@RequiredArgsConstructor
public class OrganizerServiceImpl implements OrganizerService {

    private final OrganizerRepository organizerRepository;  // Ensure final and injected via constructor

    private static final String UPLOAD_DIR = "/uploads/";

    @Override
    public Organizer createOrganizer(OrganizerRequest organizerRequest) {
        System.out.println("Organizer Service Called");
        try {
            Organizer organizer = new Organizer();

            // Map fields from DTO to Entity
            organizer.setOrgName(organizerRequest.getOrgName());
            organizer.setIrdNumber(organizerRequest.getIrdNumber());
            organizer.setOrgType(organizerRequest.getOrgType());
            organizer.setCharityRegistration(organizerRequest.getCharityRegistration());
            organizer.setDoneeStatus(organizerRequest.getDoneeStatus());
            organizer.setPreferredOrgName(organizerRequest.getPreferredOrgName());
            organizer.setOverview(organizerRequest.getOverview());
            organizer.setCauseAreas(organizerRequest.getCauseAreas()); // Comma-separated string

            // Save files and store paths
            organizer.setLogo(saveFile(organizerRequest.getLogo()));
            organizer.setPaymentBankStatement(saveFile(organizerRequest.getPaymentBankStatement()));
            organizer.setSettlementBankStatement(saveFile(organizerRequest.getSettlementBankStatement()));

            // Address Details
            organizer.setStreetAddress(organizerRequest.getStreetAddress());
            organizer.setSuburb(organizerRequest.getSuburb());
            organizer.setCity(organizerRequest.getCity());
            organizer.setPostalCode(organizerRequest.getPostalCode());
            organizer.setCountry(organizerRequest.getCountry());

            // Admin Contact
            organizer.setAdminPosition(organizerRequest.getAdminPosition());
            organizer.setAdminSalutation(organizerRequest.getAdminSalutation());
            organizer.setAdminFirstName(organizerRequest.getAdminFirstName());
            organizer.setAdminLastName(organizerRequest.getAdminLastName());
            organizer.setAdminEmail(organizerRequest.getAdminEmail());
            organizer.setAdminPhone(organizerRequest.getAdminPhone());

            // Finance Contact
            organizer.setFinanceEmail(organizerRequest.getFinanceEmail());
            organizer.setFinanceFirstName(organizerRequest.getFinanceFirstName());
            organizer.setFinanceLastName(organizerRequest.getFinanceLastName());
            organizer.setFinancePhone(organizerRequest.getFinancePhone());
            organizer.setFinancePosition(organizerRequest.getFinancePosition());

            // Payment and Settlement Details
            organizer.setPaymentAccountNumber(organizerRequest.getPaymentAccountNumber());
            organizer.setPaymentBankName(organizerRequest.getPaymentBankName());
            organizer.setSettlementAccountNumber(organizerRequest.getSettlementAccountNumber());
            organizer.setSettlementBankName(organizerRequest.getSettlementBankName());

            // Other Fields
            organizer.setSameAsAbove(organizerRequest.isSameAsAbove());
            organizer.setTerms(organizerRequest.isTerms());

            // Save the organizer entity to the database
            return organizerRepository.save(organizer);

        } catch (IOException e) {
            throw new RuntimeException("Error while saving organizer: " + e.getMessage());
        }
    }

    private String saveFile(MultipartFile file) throws IOException {
        if (file != null && !file.isEmpty()) {
            Path uploadDir = Paths.get(UPLOAD_DIR);
            Files.createDirectories(uploadDir); // Ensure the directory exists
            Path filePath = uploadDir.resolve(file.getOriginalFilename());
            Files.write(filePath, file.getBytes());
            return filePath.toString(); // Return the file path to be saved in the database
        }
        return null;
    }
}