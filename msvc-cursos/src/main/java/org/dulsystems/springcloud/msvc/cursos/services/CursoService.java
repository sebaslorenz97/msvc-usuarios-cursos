package org.dulsystems.springcloud.msvc.cursos.services;

import org.dulsystems.springcloud.msvc.cursos.models.Usuario;
import org.dulsystems.springcloud.msvc.cursos.models.entity.Curso;

import java.util.List;
import java.util.Optional;

public interface CursoService {

    //Services that work with data from DB through Data Base Connector
    List<Curso> listar();

    Optional<Curso> porId(Long id);

    Curso guardar(Curso curso);

    void eliminar(Long id);

    //Remote Services or Remote Methods that work with data from other Microservice through HTTP client or API REST
    Optional<Usuario> asignarUsuario(Usuario usuario, Long cursoId);

    Optional<Usuario> crearUsuario(Usuario usuario, Long cursoId);

    Optional<Usuario> eliminarUsuario(Usuario usuario, Long cursoId);

    Optional<Curso> porIdConUsuarios(Long id);

    void eliminarCursoUsuarioPorId(Long id);

}
