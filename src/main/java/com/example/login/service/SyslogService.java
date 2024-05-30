package com.example.login.service;

import com.example.login.DTO.MonthlyRecordCountDTO;
import com.example.login.DTO.SysLogDTO;
import com.example.login.repository.SyslogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Object[]> results = syslogRepository.findCountByMonth(
                sysLogDTO.getStartDate(), sysLogDTO.getEndDate(), sysLogDTO.getMethod()
        );

        return results.stream()
                .map(record -> {
                    String month = String.format("%d/%02d", (int) record[0], (int) record[1]);
                    long count = ((Number) record[2]).longValue();
                    return new MonthlyRecordCountDTO(month, count);
                })
                .collect(Collectors.toList());
    }
}
