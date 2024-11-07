package ca.sheridancollege.packofbag.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import ca.sheridancollege.packofbag.repository.VolunteerRepository;

@Service
public class VolunteerService {

    @Autowired
    private VolunteerRepository volunteerRepository;

    public long countVolunteers() {
        return volunteerRepository.count(); // This will return the total number of volunteers
    }
}
