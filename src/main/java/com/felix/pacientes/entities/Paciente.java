package com.felix.pacientes.entities;


import com.felix.pacientes.enums.EstadoRegistro;
import com.felix.pacientes.utils.StringCustomUtils;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

@Entity
@Table(name = "PACIENTES")
@AllArgsConstructor
@NoArgsConstructor
@Builder @Getter
public class Paciente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID_PACIENTE")
    Long id;

    @Column(name = "NOMBRE", nullable = false, length = 50)
    String nombre;

    @Column(name = "APELLIDO_PATERNO", nullable = false, length = 50)
    String apellidoPaterno;

    @Column(name = "APELLIDO_MATERNO", nullable = false, length = 50)
    String apellidoMaterno;

    @Column(name = "EDAD", nullable = false)
    Short edad;

    @Column(name = "PESO", nullable = false)
    Double peso;

    @Column(name = "ESTATURA", nullable = false)
    Double estatura;

    @Column(name = "IMC", nullable = false)
    Double imc;

    @Column(name = "EMAIL", nullable = false, length = 100)
    String email;

    @Column(name = "NUM_EXPEDIENTE", nullable = false, length = 20)
    String numExpediente;

    @Column(name = "TELEFONO", nullable = false, length = 10)
    String telefono;

    @Column(name = "DIRECCION", nullable = false, length = 150)
    String direccion;

    @Enumerated(EnumType.STRING)
    @JdbcTypeCode(SqlTypes.NAMED_ENUM)
    @Column(name = "ESTADO_REGISTRO", nullable = false)
    EstadoRegistro estadoRegistro;


    private void validarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String numExpediente,
            String telefono,
            String direccion) {

        StringCustomUtils.validarTamanio(nombre, 1, 50,
                "El nombre es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoPaterno, 1, 50,
                "El apellido paterno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(apellidoMaterno, 1, 50,
                "El apellido materno es requerido y debe tener entre 1 y 50 caracteres");

        StringCustomUtils.validarTamanio(email, 1, 100,
                "El email es requedrido y debe tener entre 1 y 100 caracteres");

        StringCustomUtils.validarTamanio(numExpediente, 1, 20,
                "El numero de expediente es requerido y debe tener entre 1 y 20 caracteres");

        StringCustomUtils.validarTamanio(telefono, 10, 10,
                "El telefono es requerido y debe tener 10 caracteres");

        StringCustomUtils.validarTamanio(direccion, 1, 150,
                "La direccion es requerida y debe tener entre 1 y 150 caracteres");
    }

    public void actualizarDatos(
            String nombre,
            String apellidoPaterno,
            String apellidoMaterno,
            String email,
            String numExpediente,
            String telefono,
            String direccion
    ) {
        validarDatos(
                nombre,
                apellidoPaterno,
                apellidoMaterno,
                email,
                numExpediente,
                telefono,
                direccion
                );

        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.email = email;
        this.numExpediente = numExpediente;
        this.telefono = telefono;
        this.direccion = direccion;
    }


}
