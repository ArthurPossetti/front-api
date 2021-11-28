package br.edu.unifio.setimoprojeto.resource;


import br.edu.unifio.setimoprojeto.domain.Cliente;
import br.edu.unifio.setimoprojeto.repository.ClienteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("clientes")
public class ClienteResource {
    @Autowired
    private ClienteRepository clienteRepository;

    @GetMapping
    public List<Cliente> listarClientes(){
        List<Cliente> clientes = clienteRepository.findAll(Sort.by(Sort.Direction.ASC,"nome"));
        return clientes;
    }

    @PostMapping
    public Cliente inserirCliente (@RequestBody Cliente cliente){
        Cliente clienteSalvo = clienteRepository.save(cliente);
        return clienteSalvo;
    }

    @DeleteMapping("{codigo}")
    public void excluir (@PathVariable Integer codigo){
        clienteRepository.deleteById(codigo);
    }


    @PutMapping("editarCliente/{codigo}")
    public Cliente editarCliente (@PathVariable Integer codigo, @RequestBody Cliente clienteNovo){
        Optional<Cliente> opcional = Optional.of(clienteRepository.findById(codigo).get());
        Cliente clienteEdicao = opcional.get();

        clienteEdicao.setNome(clienteNovo.getNome());
        clienteEdicao.setCpf(clienteNovo.getCpf());
        clienteEdicao.setEmail(clienteNovo.getEmail());
        clienteEdicao.setSenha(clienteNovo.getSenha());

        Cliente clienteSalvo = clienteRepository.save(clienteEdicao);
        return clienteSalvo;
    }
}
