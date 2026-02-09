package org.example.models;
import jakarta.persistence.*;
import org.example.models.EstadoOrden;

import java.time.LocalDateTime;
import java.util.Date;
@Entity
@Table(name="OrdenesReparacion")
public class Orden {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idOrden")
    private int idOrden;

    @ManyToOne
    @JoinColumn(name = "idCliente", nullable = false)
    private Cliente cliente;

    @Column(name = "fechaIngreso")
    private LocalDateTime fechaIngreso;

    @Column(name = "descripcionProblema")
    private String descripcion;


    @Convert(converter = EstadoOrdenConverter.class) // Usamos el converter para mapear el enum
    @Column(name = "estado")
    private EstadoOrden estado;

    public Orden() {
    }

    public Orden(Cliente cliente, LocalDateTime fechaIngreso, String descripcion, EstadoOrden estado) {
        this.cliente = cliente;
        this.fechaIngreso = fechaIngreso;
        this.descripcion = descripcion;
        this.estado = estado;
    }

    public int getIdOrden() {
        return idOrden;
    }

    public void setIdOrden(int idOrden) {
        this.idOrden = idOrden;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public LocalDateTime getFecha() {
        return fechaIngreso;
    }

    public void setFecha(LocalDateTime fecha) {
        this.fechaIngreso = fecha;
    }

    public Cliente getCliente() {
        return cliente;
    }

    public void setCliente(Cliente cliente) {
        this.cliente = cliente;
    }

    public EstadoOrden getEstado() {
        return estado;
    }

    public void setEstado(EstadoOrden estado) {
        this.estado = estado;
    }
}
