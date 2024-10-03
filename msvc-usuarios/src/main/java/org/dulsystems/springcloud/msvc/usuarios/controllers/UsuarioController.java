package org.dulsystems.springcloud.msvc.usuarios.controllers;

import jakarta.validation.Valid;
import org.dulsystems.springcloud.msvc.usuarios.models.entity.Usuario;
import org.dulsystems.springcloud.msvc.usuarios.services.UsuarioService;
import org.dulsystems.springcloud.msvc.usuarios.services.UsuarioServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
public class UsuarioController {

    @Autowired
    private UsuarioService service;

    @GetMapping
    public List<Usuario> listar(){
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> detalle(@PathVariable Long id){
        Optional<Usuario> usuarioOptional = service.porId(id);
        if(usuarioOptional.isPresent()){
            return ResponseEntity.ok().body(usuarioOptional.get());
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping
    public ResponseEntity<?> crear(@Valid @RequestBody Usuario usuario, BindingResult result){

        if(result.hasErrors()){
            return validar(result);
        }
        if(!usuario.getEmail().isEmpty() && service.existePorEmail(usuario.getEmail())){
            return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico"));
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuario));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> editar(@Valid @RequestBody Usuario usuario, BindingResult result, @PathVariable Long id){
        if(result.hasErrors()){
            return validar(result);
        }

        Optional<Usuario> o = service.porId(id);
        if(o.isPresent()){
            Usuario usuarioDb = o.get();

            if(!usuario.getEmail().isEmpty()
               && !usuario.getEmail().equalsIgnoreCase(usuarioDb.getEmail())
               && service.porEmail(usuario.getEmail()).isPresent()){
                    return ResponseEntity.badRequest().body(Collections.singletonMap("mensaje", "Ya existe un usuario con ese correo electronico"));
            }

            usuarioDb.setNombre(usuario.getNombre());
            usuarioDb.setEmail(usuario.getEmail());
            usuarioDb.setPassword(usuario.getPassword());
            return ResponseEntity.status(HttpStatus.CREATED).body(service.guardar(usuarioDb));
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Long id){
        Optional<Usuario> o = service.porId(id);
        if(o.isPresent()){
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/usuarios-por-curso")
    public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
        return ResponseEntity.ok(service.listarPorIds(ids));
    }

    //UTIL METHODS
    private ResponseEntity<Map<String, String>> validar(BindingResult result) {
        Map<String, String> errores = new HashMap<>();
        result.getFieldErrors().forEach(error -> {
            errores.put(error.getField(), "El campo" + error.getField() + " " + error.getDefaultMessage());
        });
        return ResponseEntity.badRequest().body(errores);
    }

}
