package com.felix.pacientes.enums;

import com.felix.pacientes.utils.StringCustomUtils;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum EstadoRegistro {

    ACTIVO ("Activo"),
    ELIMINADO("Eliminado");

    private final String descripcion;

    public static EstadoRegistro obtenerEstadoRegistroPorDescripcion(String descripcion) {
        StringCustomUtils.validarNoVacio(descripcion, "La descripcion es requerida");

        String descripcionNormalizada = StringCustomUtils.quitarAcentos(descripcion);

        for (EstadoRegistro estadoRegistro : values()) {
            if (StringCustomUtils.quitarAcentos(estadoRegistro.getDescripcion()).equalsIgnoreCase(descripcionNormalizada)) {
                return estadoRegistro;
            }
        }
        throw new IllegalArgumentException("No existe un estado de registro con la descripcion: " + descripcion);
    }
}
