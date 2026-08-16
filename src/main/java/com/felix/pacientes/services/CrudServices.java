package com.felix.pacientes.services;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CrudServices<RQ, RS> {

    List<RS> listar();

    RS obtenerPorId(Long id);

    RS registrar(RQ request);

    RS actualizar(RQ request, Long id);

    void eliminar(Long id);
}
