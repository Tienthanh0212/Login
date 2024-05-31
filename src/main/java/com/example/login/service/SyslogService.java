package com.example.login.service;

import com.example.login.DTO.MonthlyRecordCountDTO;
import com.example.login.DTO.SysLogDTO;
import com.example.login.repository.SyslogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
public interface SyslogService {
List<MonthlyRecordCountDTO> getMonthlyRecordCountsBetweenDates(SysLogDTO sysLogDTO);
}
@Service
class SyslogServiceImpl implements  SyslogService{

    @Autowired
    private SyslogRepository syslogRepository;

    @Override
    public List<MonthlyRecordCountDTO> getMonthlyRecordCountsBetweenDates(SysLogDTO sysLogDTO) {
        Date startDate = Date.from(sysLogDTO.getStartDate().atDay(1).atStartOfDay(ZoneId.systemDefault()).toInstant());
        Date endDate = Date.from(sysLogDTO.getEndDate().atEndOfMonth().atStartOfDay(ZoneId.systemDefault()).toInstant());

        List<Object[]> results = syslogRepository.findCountByMonth(startDate, endDate, sysLogDTO.getMethod());
        return results.stream()
                .map(record -> {
                    String month = String.format("%d/%02d", (int) record[0], (int) record[1]);
                    Number count = (Number) record[2]; // Cast to Number
                    // If count is not null and equals 0, set it to null
                    count = (count != null && count.intValue() == 0) ? null : count;
                    return new MonthlyRecordCountDTO(month, count);
                })
                .collect(Collectors.toList());
    }
}
