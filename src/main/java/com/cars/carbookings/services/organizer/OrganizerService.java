package com.cars.carbookings.services.organizer;

import com.cars.carbookings.dto.OrganizerRequest;
import com.cars.carbookings.entities.Organizer;

public interface OrganizerService {

    public Organizer createOrganizer(OrganizerRequest organizerRequest);
}
