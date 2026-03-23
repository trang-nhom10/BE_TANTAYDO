package com.example.da_tantaydo.service;

import com.example.da_tantaydo.model.dto.response.AgeDistributionResponseDTO;
import com.example.da_tantaydo.model.dto.response.MonthlyStatResponseDTO;
import com.example.da_tantaydo.model.dto.response.OverviewResponseDTO;
import com.example.da_tantaydo.model.dto.response.StatusRateResponseDTO;

import java.util.List;

public interface DashboardService {
    OverviewResponseDTO getOverview();
    List<AgeDistributionResponseDTO> getAgeDistribution();
    List<StatusRateResponseDTO> getStatusRate();
    List<MonthlyStatResponseDTO> getMonthlyStats();
}