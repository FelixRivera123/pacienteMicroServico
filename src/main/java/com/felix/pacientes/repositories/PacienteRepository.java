package com.felix.pacientes.repositories;

import com.felix.pacientes.entities.Paciente;
import com.felix.pacientes.enums.EstadoRegistro;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, Long> {

    List<Paciente> findAllByEstadoRegistro(EstadoRegistro estadoRegistro);

    Optional<Paciente> findByIdAndEstadoRegistro(
            Long id,
            EstadoRegistro estadoRegistro
    );

    boolean existsByEmailIgnoreCaseAndEstadoRegistro(
            String email,
            EstadoRegistro estadoRegistro
    );

    boolean existsByTelefonoAndEstadoRegistro(
            String telefono,
            EstadoRegistro estadoRegistro);
}
