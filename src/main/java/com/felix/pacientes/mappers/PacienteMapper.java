package com.felix.pacientes.mappers;

import com.felix.pacientes.dtos.PacienteRequest;
import com.felix.pacientes.dtos.PacienteResponse;
import com.felix.pacientes.entities.Paciente;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class PacienteMapper implements CommonMapper<PacienteRequest, PacienteResponse, Paciente> {

    @Override
    public Paciente requestAEntidad(PacienteRequest request) {
        if (request == null) return null;

        return Paciente.builder()
                .nombre(request.nombre().trim())
                .apellidoPaterno(request.apellidoPaterno().trim())
                .apellidoMaterno(request.apellidoMaterno().trim())
                .edad(request.edad())
                .peso(request.peso())
                .estatura(request.estatura())
                .email(request.email().toLowerCase().trim())
                .telefono(request.telefono().trim())
                .direccion(request.direccion().trim())
                .build();
    }

    @Override
    public PacienteResponse entidadAResponse(Paciente entidad) {
        if (entidad == null) return null;

        return new PacienteResponse(
                entidad.getId(),
                String.join(" ",
                        entidad.getNombre(),
                        entidad.getApellidoPaterno(),
                        entidad.getApellidoMaterno()),
                entidad.getEdad(),
                entidad.getPeso(),
                entidad.getEstatura(),
                entidad.getImc(),
                entidad.getEmail(),
                entidad.getTelefono(),
                entidad.getDireccion(),
                entidad.getNumExpediente()
        );
    }
}
