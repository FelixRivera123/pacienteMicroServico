package com.felix.pacientes.services.paciente;

import com.felix.pacientes.dtos.PacienteRequest;
import com.felix.pacientes.dtos.PacienteResponse;
import com.felix.pacientes.entities.Paciente;
import com.felix.pacientes.enums.EstadoRegistro;
import com.felix.pacientes.exceptions.EntidadRelacionadaException;
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

        return pacienteMapper.entidadAResponse(obtenerPacienteActivo(id));
    }

    @Override
    public PacienteResponse registrar(PacienteRequest request) {

        log.info("Validando duplicados");

        validarDuplicados(
                request.email(),
                request.telefono()
        );

        Paciente paciente = pacienteMapper.requestAEntidad(request);

        log.info("Calculando Imc");

        Double imc = calcularImc(
                paciente.getPeso(),
                paciente.getEstatura()
        );

        log.info("Generando Numero de Expediente");

        String expediente = generarNumeroExpediente(
                paciente.getTelefono()
        );

        log.info("Generando Paciente");

        paciente.asignarDatosRegistro(
                imc,
                expediente,
                EstadoRegistro.ACTIVO
        );

        pacienteRepository.save(paciente);

        log.info("Nuevo paciente {} registrado correctamente", paciente.getNombre());

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public PacienteResponse actualizar(PacienteRequest request, Long id) {

        Paciente paciente = obtenerPacienteActivo(id);

        log.info("Registrando nuevo Paciente...");

        validarDuplicadosActualizacion(
                request.email(),
                request.telefono(),
                id
        );

        Double imc = calcularImc(
                request.peso(),
                request.estatura()
        );

        String numExpediente = generarNumeroExpediente(request.telefono());

        paciente.actualizarDatos(
                request.nombre().trim(),
                request.apellidoPaterno().trim(),
                request.apellidoMaterno().trim(),
                request.edad(),
                request.peso(),
                request.estatura(),
                imc,
                request.email().toLowerCase().trim(),
                numExpediente,
                request.telefono().trim(),
                request.direccion().trim()
        );

        pacienteRepository.save(paciente);

        log.info("Datos del paciente actualizados...");

        return pacienteMapper.entidadAResponse(paciente);
    }

    @Override
    public void eliminar(Long id) {

        Paciente paciente = obtenerPacienteActivo(id);
        paciente.eliminar();
        pacienteRepository.save(paciente);
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

    private void validarDuplicados(String email, String telefono) {

        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistro(
                email,
                EstadoRegistro.ACTIVO)) {

            throw new EntidadRelacionadaException(
                    "Ya existe un paciente activo con ese email"
            );
        }

        if (pacienteRepository.existsByTelefonoAndEstadoRegistro(
                telefono,
                EstadoRegistro.ACTIVO)) {

            throw new EntidadRelacionadaException(
                    "Ya existe un paciente activo con ese teléfono"
            );
        }
    }

    private void validarDuplicadosActualizacion(
            String email,
            String telefeno,
            Long id
    ){
        if (pacienteRepository.existsByEmailIgnoreCaseAndEstadoRegistroAndIdNot(
                email,
                EstadoRegistro.ACTIVO,
                id
        )) throw new EntidadRelacionadaException(
                "Ya existe otro paciente activo con ese email"
        );
        if (pacienteRepository.existsByTelefonoAndEstadoRegistroAndIdNot(
                telefeno,
                EstadoRegistro.ACTIVO,
                id
        )) throw new EntidadRelacionadaException(
                "Ya existe otro paciente activo con ese teléfono"
        );
    }
}
