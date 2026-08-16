package com.felix.pacientes.services.paciente;

import com.felix.pacientes.dtos.PacienteRequest;
import com.felix.pacientes.dtos.PacienteResponse;
import com.felix.pacientes.entities.Paciente;
import com.felix.pacientes.enums.EstadoRegistro;
import com.felix.pacientes.exceptions.RecursoNoEncontradoException;
import com.felix.pacientes.mappers.PacienteMapper;
import com.felix.pacientes.repositories.PacienteRepository;
import com.felix.pacientes.services.ServicesUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class PacienteServiceImpl implements PacienteService {

    private final PacienteRepository pacienteRepository;
    private final PacienteMapper pacienteMapper;

    @Override
    @Transactional(readOnly = true)
    public List<PacienteResponse> listar() {
        log.info("Listando todos los pacientes");

        return pacienteRepository
                .findAllByEstadoRegistro(EstadoRegistro.ACTIVO)
                .stream()
                .map(pacienteMapper::entidadAResponse)
                .toList();
    }

    @Override
    public PacienteResponse obtenerPorId(Long id) {
        log.info("Obteniendo paciente por id: {}", id);

        return pacienteMapper.entidadAResponse(obtenerPaciente(id));
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {
        Paciente paciente = pacienteMapper.requestAEntidad(request);

        Double imc = calcularImc(
                paciente.getPeso(),
                paciente.getEstatura()
        );

        String expediente = generarNumeroExpediente(
                paciente.getTelefono()
        );

        paciente.asignarDatosRegistro(
                imc,
                expediente,
                EstadoRegistro.ACTIVO
        );

        pacienteRepository.save(paciente);

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {
        return null;
    }

    @Override
    public void eliminar(Long id) {

        Paciente paciente = obtenerPaciente(id);
        paciente.eliminar();
        pacienteRepository.save(paciente);
    }

    private Paciente obtenerPaciente(Long id){
        return ServicesUtils.onbtenerEntidadOException(pacienteRepository, id, Paciente.class);
    }

    private Paciente obtenerPacienteActivo(Long id){
        return pacienteRepository
                .findByIdAndEstadoRegistro(id, EstadoRegistro.ACTIVO)
                .orElseThrow(() ->
                        new RecursoNoEncontradoException("Paciente no encontrado"));
    }

    private Paciente obtenerPacienteSinValidarEstado(Long id){
        return pacienteRepository
                .findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Paciente no encontrado"));
    }

    private Double calcularImc(Double peso, Double altura){
        return peso/(altura*altura);
    }

    private String generarNumeroExpediente(String telefono) {
        StringBuilder numeroExpediente = new StringBuilder();

        for (char numero : telefono.toCharArray()) {
            numeroExpediente.append(numero).append("X");
        }

        return numeroExpediente.toString();
    }

}
