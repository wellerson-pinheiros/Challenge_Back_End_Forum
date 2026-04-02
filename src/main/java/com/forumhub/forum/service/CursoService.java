package com.forumhub.forum.service;

import com.forumhub.forum.domain.Categoria;
import com.forumhub.forum.domain.Curso;
import com.forumhub.forum.dto.CursoDTO;
import com.forumhub.forum.excecoes.ResourceAlreadyRegistered;
import com.forumhub.forum.excecoes.ResourceNotFoundException;
import com.forumhub.forum.repositorio.CategoriaRepositorio;
import com.forumhub.forum.repositorio.CursoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CursoService {

    @Autowired
    CursoRepository cursoRepository;

    @Autowired
    CategoriaRepositorio categoriaRepositorio;


    public List<CursoDTO> findAll() {
        List <Curso> cursos = cursoRepository.findAll();
        return cursos.stream().map(CursoDTO::new).toList();
    }

    public List<CursoDTO> findAllByAtivo() {
        List<Curso> cursos = cursoRepository.findByAtivoTrue();
        return cursos.stream().map(CursoDTO::new).toList();
    }

    public CursoDTO findById(Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Curso com o id " + id + " Não encontrado"));
        return new CursoDTO(curso);
    }

    public List<CursoDTO> findAllByCategoria(String nomeCategoria) {
       List<Curso> cursos = cursoRepository.findByCategoriaNomeLike(nomeCategoria);
                if(cursos.isEmpty()){
                    throw new ResourceNotFoundException("Categoria não encontrada");
                }
        return cursos.stream().map(CursoDTO::new).toList();
    }

    @Transactional
    public void save(CursoDTO cursoDTO) {

        // 1. Verifica se o curso já existe
        Optional<Curso> cursobuscado = cursoRepository.findByNomeIgnoreCase(cursoDTO.nome());
        if (cursobuscado.isPresent()) {
            throw new ResourceAlreadyRegistered("Curso com o nome " + cursoDTO.nome() + " já registrado!");
        }



        // 2. Verifica se a categoria existe
        Categoria categoria = categoriaRepositorio.findByNomeIgnoreCase(cursoDTO.categoriaSimplesDTO().nome()).orElseGet(() -> {
            Categoria novaCat = new Categoria();
            novaCat.setNome(cursoDTO.categoriaSimplesDTO().nome());
            novaCat.setDescricao(cursoDTO.categoriaSimplesDTO().descricao());
            return categoriaRepositorio.save(novaCat);
        });

        // 3. caso não existir a categoria e o curso, salva no banco de dados

        Curso curso = new Curso();
        curso.setId(cursoDTO.id());
        curso.setNome(cursoDTO.nome());
        curso.setDescricao(cursoDTO.descricao());
        curso.setCategoria(categoria);
        cursoRepository.save(curso);
    }

    @Transactional
    public void delete(Long id) {
        Curso curso = cursoRepository.findById(id).orElseThrow(()-> new ResourceNotFoundException("Curso não encontrado" + id));
        curso.setAtivo(false);
        cursoRepository.save(curso);
    }

}