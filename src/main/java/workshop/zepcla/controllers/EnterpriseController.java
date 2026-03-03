package workshop.zepcla.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.AllArgsConstructor;
import workshop.zepcla.dto.enterpriseDto.EnterpriseCreationDto;
import workshop.zepcla.dto.enterpriseDto.EnterpriseDto;
import workshop.zepcla.entities.EnterpriseEntity;
import workshop.zepcla.services.EnterpriseService;

@RestController
@AllArgsConstructor
@RequestMapping("/enterprises")
public class EnterpriseController {

    private final EnterpriseService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnterpriseEntity> createEnterprise(@RequestBody EnterpriseCreationDto dto) {
        return new ResponseEntity<>(service.createEnterprise(dto), HttpStatus.CREATED);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Iterable<EnterpriseEntity>> getAllEnterprises() {
        return ResponseEntity.ok(service.getAllEnterprises());
    }

    @GetMapping("/MyEnterprise")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnterpriseEntity> getEnterpriseById() {
        return ResponseEntity.ok(service.getEnterpriseById());
    }

    @GetMapping("/MyEnterprise/details")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnterpriseDto> getEnterpriseWithDetails() {
        return ResponseEntity.ok(service.getEnterpriseWithDetails());
    }

    @GetMapping("/MyEnterprise/breaks")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnterpriseDto> getEnterpriseBreakDetails() {
        return ResponseEntity.ok(service.getEnterpriseBreakDetails());
    }

    @GetMapping("/MyEnterprise/holidays")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnterpriseDto> getEnterpriseHolidayDetails() {
        return ResponseEntity.ok(service.getEnterpriseHolidayDetails());
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('SUPERADMIN')")
    public ResponseEntity<Void> deleteEnterprise(@PathVariable Long id) {
        service.deleteEnterprise(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/MyEnterprise/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<EnterpriseEntity> updateEnterprise(
            @RequestBody EnterpriseCreationDto dto) {
        return ResponseEntity.ok(service.updateEnterprise(dto));
    }
}
