package com.forumhub.forum.service;

import com.forumhub.forum.domain.Categoria;
import com.forumhub.forum.dto.CategoriaSimplesDTO;
import com.forumhub.forum.excecoes.ResourceAlreadyRegistered;
import com.forumhub.forum.excecoes.ResourceNotFoundException;
import com.forumhub.forum.repositorio.CategoriaRepositorio;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CategoriaService {

    @Autowired
    private CategoriaRepositorio categoriaRepositorio;

    public List<CategoriaSimplesDTO> findAll() {
      List<Categoria> categorias =  categoriaRepositorio.findByAtivoTrue();
        // Converte cada entidade para DTO usando o construtor
        return categorias.stream()
                .map(CategoriaSimplesDTO::new) // chama o construtor da record que recebe a entidade
                .toList();
    }

    public CategoriaSimplesDTO findById(Long id){
        Categoria categoria = categoriaRepositorio.findById(id).orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));
        return new CategoriaSimplesDTO(categoria);
    }

    @Transactional
    public void save (CategoriaSimplesDTO categoriaSimplesDTO){

        Optional<Categoria> buscaCategoria = categoriaRepositorio.findByNome(categoriaSimplesDTO.nome());

        if(buscaCategoria.isPresent()){
            Categoria categoria1 = buscaCategoria.get();
            throw new ResourceAlreadyRegistered("Categoria já cadastrada " + categoria1.getNome());
        }else {
            Categoria categoria = new Categoria(categoriaSimplesDTO.nome(),categoriaSimplesDTO.descricao());
            categoriaRepositorio.save(categoria);
        }
    }

    @Transactional
    public void delete(Long id) {
        Categoria categoria = categoriaRepositorio.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Categoria não encontrada"));

        // Soft delete: apenas marca como inativa
        categoria.setAtivo(false);

        // Salva a alteração
        categoriaRepositorio.save(categoria);
    }
}
