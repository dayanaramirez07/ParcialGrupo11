package com.Grupo11.Exam.controller;

import com.Grupo11.Exam.model.*;
import com.Grupo11.Exam.repository.*;
import com.Grupo11.Exam.dto.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Controller
public class GraphQLController {

    @Autowired
    private ProyectoRepository proyectoRepository;

    @Autowired
    private EmpleadoRepository empleadoRepository;

    // Resolver para la Query: proyectosPorEmpleado
    @QueryMapping
    public List<Proyecto> proyectosPorEmpleado(@Argument String cedula) {
        System.out.println("Cédula recibida: " + cedula);
        return proyectoRepository.findByEmpleadoCedula(cedula);
    }

    // Resolver para la Mutation: registrarProyecto
    @MutationMapping
    public Proyecto registrarProyecto(
            @Argument String nombre,
            @Argument String descripcion,
            @Argument String fechaInicio,
            @Argument String fechaFin,
            @Argument String estado,
            @Argument List<EmpleadoInput> empleados) {

        // Imprimir los parámetros recibidos para depurar
        System.out.println("Nombre: " + nombre);
        System.out.println("Descripción: " + descripcion);
        System.out.println("Fecha Inicio: " + fechaInicio);
        System.out.println("Fecha Fin: " + fechaFin);
        System.out.println("Estado: " + estado);
        System.out.println("Empleados: " + empleados);

        // Crear y guardar el proyecto
        Proyecto proyecto = new Proyecto();
        proyecto.setNombre(nombre);
        proyecto.setDescripcion(descripcion);
        proyecto.setFechaInicio(LocalDate.parse(fechaInicio));
        proyecto.setFechaFin(LocalDate.parse(fechaFin));
        proyecto.setEstado(estado);

        List<Empleado> empleadosList = empleados.stream().map(input -> {
            Empleado empleado = new Empleado();
            empleado.setNombre(input.getNombre());
            empleado.setApellido(input.getApellido());
            empleado.setCedula(input.getCedula());
            empleado.setRol(input.getRol());
            empleado.setProyecto(proyecto);
            return empleado;
        }).collect(Collectors.toList());

        proyecto.setEmpleados(empleadosList);

        // Guardar el proyecto y empleados en la base de datos
        return proyectoRepository.save(proyecto);
    }
}