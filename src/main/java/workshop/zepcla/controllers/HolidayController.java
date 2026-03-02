package workshop.zepcla.controllers;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.parameters.RequestBody;
import lombok.AllArgsConstructor;
import workshop.zepcla.dto.holidayDto.HolidayCreationDto;
import workshop.zepcla.services.HolidayService;

@RestController
@AllArgsConstructor
@RequestMapping("/holidays")
public class HolidayController {

    private final HolidayService service;

    @PostMapping("/create")
    public void createHoliday(@RequestBody HolidayCreationDto dto) {
        service.createHoliday(dto);
    }

    @RequestMapping("/getAll")
    public void getAllHolidays() {
        service.getAllHolidays();
    }

    @RequestMapping("/getById")
    public void getHolidayById(Long id) {
        service.getHolidayById(id);
    }

    @DeleteMapping("/delete/{id}")
    public void deleteHoliday(Long id) {
        service.deleteHoliday(id);
    }

    @PutMapping("/update/{id}")
    public void updateHoliday(Long id, @RequestBody HolidayCreationDto dto) {
        service.updateHoliday(id, dto);
    }
}
