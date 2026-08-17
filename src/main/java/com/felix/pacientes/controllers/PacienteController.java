package com.felix.pacientes.controllers;

import com.felix.pacientes.dtos.PacienteRequest;
import com.felix.pacientes.dtos.PacienteResponse;
import com.felix.pacientes.services.paciente.PacienteService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/pacientes")
public class PacienteController extends CommonController <PacienteRequest, PacienteResponse, PacienteService>{

    public PacienteController(PacienteService service) {
        super(service);
    }
}
