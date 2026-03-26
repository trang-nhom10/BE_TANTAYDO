package com.example.da_tantaydo.service.impl;

import com.example.da_tantaydo.model.dto.response.AgeDistributionResponseDTO;
import com.example.da_tantaydo.model.dto.response.MonthlyStatResponseDTO;
import com.example.da_tantaydo.model.dto.response.OverviewResponseDTO;
import com.example.da_tantaydo.model.dto.response.StatusRateResponseDTO;
import com.example.da_tantaydo.repository.AppointmentRepository;
import com.example.da_tantaydo.service.DashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DashboardServiceImpl implements DashboardService {

    private final AppointmentRepository appointmentRepository;

    @Override
    public OverviewResponseDTO getOverview() {
        Object[] stats = (Object[]) appointmentRepository.getOverviewStats()[0];
        long totalToday        = ((Number) stats[0]).longValue();
        long totalYesterday    = ((Number) stats[1]).longValue();
        long totalPending      = ((Number) stats[2]).longValue();
        long pendingYesterday  = ((Number) stats[3]).longValue();
        long totalConfirmed    = ((Number) stats[4]).longValue();
        long confirmedYesterday= ((Number) stats[5]).longValue();
        long totalCancelled    = ((Number) stats[6]).longValue();
        long cancelledYesterday= ((Number) stats[7]).longValue();
        long totalDoctors      = ((Number) stats[8]).longValue();
        long totalCustomers    = ((Number) stats[9]).longValue();

        return OverviewResponseDTO.builder()
                .totalBooked(totalToday)
                .totalBookedChange(calcChange(totalToday, totalYesterday))
                .totalPending(totalPending)
                .totalPendingChange(calcChange(totalPending, pendingYesterday))
                .totalConfirmed(totalConfirmed)
                .totalConfirmedChange(calcChange(totalConfirmed, confirmedYesterday))
                .totalCancelled(totalCancelled)
                .totalCancelledChange(calcChange(totalCancelled, cancelledYesterday))
                .totalDoctors(totalDoctors)
                .totalCustomers(totalCustomers)
                .build();
    }

    @Override
    public List<AgeDistributionResponseDTO> getAgeDistribution() {
        List<Object[]> data = appointmentRepository.getAgeDistribution();
        long totalAge = data.stream().mapToLong(r -> ((Number) r[1]).longValue()).sum();
        return data.stream()
                .map(r -> AgeDistributionResponseDTO.builder()
                        .ageGroup((String) r[0])
                        .total(((Number) r[1]).longValue())
                        .percentage(totalAge > 0
                                ? Math.round((double) ((Number) r[1]).longValue() / totalAge * 1000.0) / 10.0
                                : 0)
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<StatusRateResponseDTO> getStatusRate() {
        return appointmentRepository.getStatusRate().stream()
                .map(r -> StatusRateResponseDTO.builder()
                        .status((String) r[0])
                        .total(((Number) r[1]).longValue())
                        .percentage(((Number) r[2]).doubleValue())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public List<MonthlyStatResponseDTO> getMonthlyStats() {
        return appointmentRepository.getMonthlyStats().stream()
                .map(r -> MonthlyStatResponseDTO.builder()
                        .year(((Number) r[0]).intValue())
                        .month(((Number) r[1]).intValue())
                        .total(((Number) r[2]).longValue())
                        .confirmed(((Number) r[3]).longValue())
                        .pending(((Number) r[4]).longValue())
                        .cancelled(((Number) r[5]).longValue())
                        .build())
                .collect(Collectors.toList());
    }

    private double calcChange(long today, long yesterday) {
        if (yesterday == 0) return today > 0 ? 100.0 : 0.0;
        return Math.round((double)(today - yesterday) / yesterday * 1000.0) / 10.0;
    }
}