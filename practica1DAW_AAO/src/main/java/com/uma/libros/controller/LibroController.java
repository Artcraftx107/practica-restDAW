package com.uma.libros.controller;

import com.uma.libros.model.Libro;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/libros")
public class LibroController {

    private final Map<Long, Libro> libros = new HashMap<>();
    private final AtomicLong counter = new AtomicLong();

    // GET /libros → lista todos
    @GetMapping
    public List<Libro> getAll() {
        return new ArrayList<>(libros.values());
    }

    // POST /libros → crea libro
    @PostMapping
    public ResponseEntity<Libro> create(@RequestBody Libro libro) {
        Long id = counter.incrementAndGet();
        libro.setId(id);
        libros.put(id, libro);
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(URI.create("/libros/" + id));
        return new ResponseEntity<>(libro, headers, HttpStatus.CREATED);
    }

    // GET /libros/{id} → devuelve libro o 404
    @GetMapping("/{id}")
    public ResponseEntity<Libro> getById(@PathVariable Long id) {
        Libro libro = libros.get(id);
        if (libro == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(libro);
    }

    // PUT /libros/{id} → actualiza o 404
    @PutMapping("/{id}")
    public ResponseEntity<Libro> update(@PathVariable Long id, @RequestBody Libro libro) {
        if (!libros.containsKey(id)) return ResponseEntity.notFound().build();
        libro.setId(id);
        libros.put(id, libro);
        return ResponseEntity.ok(libro);
    }

    // DELETE /libros/{id} → elimina (204 o 404)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!libros.containsKey(id)) return ResponseEntity.notFound().build();
        libros.remove(id);
        return ResponseEntity.noContent().build();
    }

    // GET /libros/autor/{autor} → buscar por autor (200 siempre)
    @GetMapping("/autor/{autor}")
    public List<Libro> getByAutor(@PathVariable String autor) {
        return libros.values().stream()
                .filter(l -> l.getAutor().equalsIgnoreCase(autor))
                .collect(Collectors.toList());
    }
}