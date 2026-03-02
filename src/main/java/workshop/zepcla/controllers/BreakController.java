package workshop.zepcla.controllers;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import workshop.zepcla.dto.holidayDto.HolidayCreationDto;
import workshop.zepcla.services.HolidayService;

@RestController
@AllArgsConstructor
@RequestMapping("/holidays")
public class BreakController {

    private final HolidayService service;

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

    @RequestMapping("/delete/{id}")
    public void deleteHoliday(Long id) {
        service.deleteHoliday(id);

    }

    @RequestMapping("/update/{id}")
    public void updateHoliday(Long id, @RequestBody HolidayCreationDto dto) {
        service.updateHoliday(id, dto);

    }
}
