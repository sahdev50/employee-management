package com.employee.management.team;

import com.employee.management.team.dtos.RequestDTO;
import com.employee.management.team.dtos.ResponseDTO;

import java.util.List;

public interface TeamService {
    List<ResponseDTO> getAllTeams();
    ResponseDTO getTeamById(Long id);
    ResponseDTO createTeam(RequestDTO dto);
    ResponseDTO updateTeam(Long id, RequestDTO dto); // Added for editing
    void deleteTeam(Long id);
}
