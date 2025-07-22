package com.example.finalproject.controller;

import com.example.finalproject.dto.PreferencesDTO;
import com.example.finalproject.service.PreferencesService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/preferences")
@CrossOrigin(origins = "http://localhost:4200")
public class PreferencesController {

    private final PreferencesService preferencesService;

    public PreferencesController(PreferencesService preferencesService) {
        this.preferencesService = preferencesService;
    }

    @GetMapping
    public ResponseEntity<List<PreferencesDTO>> getAllPreferences() {
        return ResponseEntity.ok(preferencesService.getAllPreferences());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PreferencesDTO> getPreferenceById(@PathVariable Integer id) {
        return ResponseEntity.ok(preferencesService.getPreferenceById(id));
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<PreferencesDTO>> getPreferencesByCustomerId(@PathVariable Integer customerId) {
        return ResponseEntity.ok(preferencesService.getPreferencesByCustomerId(customerId));
    }

    @PostMapping
    public ResponseEntity<PreferencesDTO> createPreference(@RequestBody PreferencesDTO preferencesDTO) {
        return ResponseEntity.status(201).body(preferencesService.createPreference(preferencesDTO));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PreferencesDTO> updatePreference(@PathVariable Integer id, @RequestBody PreferencesDTO preferencesDTO) {
        return ResponseEntity.ok(preferencesService.updatePreference(id, preferencesDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePreference(@PathVariable Integer id) {
        preferencesService.deletePreference(id);
        return ResponseEntity.noContent().build();
    }
}
