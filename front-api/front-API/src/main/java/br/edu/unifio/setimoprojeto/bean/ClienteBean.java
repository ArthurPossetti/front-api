package br.edu.unifio.setimoprojeto.bean;

import br.edu.unifio.setimoprojeto.domain.Cliente;
import ch.qos.logback.core.joran.spi.ConsoleTarget;
import com.google.gson.Gson;
import lombok.Data;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.jsoup.Jsoup;
import org.omnifaces.util.Faces;
import org.omnifaces.util.Messages;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import javax.faces.view.ViewScoped;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@ViewScoped
@Data
public class ClienteBean {
    private Cliente cliente;

    private List<Cliente> clientes;


    public void adicionarCliente(){

        cliente = new Cliente();

    }

    public void listarClientes() {
        String json = "{}";
        try {
            json = Jsoup.connect("http://localhost:3000/cliente/-1").ignoreContentType(true).get().toString();
            json = json.replace("<html>", "").replace("</html>", "").replace("<body>", "").replace("</body>", "").replace("<head>", "").replace("</head>", "");
            System.out.println(json);
        } catch(Exception excecao){
            Messages.addGlobalError(excecao.toString());
        }

        Gson gson = new Gson(); // conversor
        Cliente[] arrayClientes = gson.fromJson(json, Cliente[].class);
        clientes = Arrays.asList(arrayClientes);
    }

    public void editarCliente(Cliente cliente) {
        Faces.setFlashAttribute("Cliente", cliente);
        Faces.navigate("cliente-editar.xhtml?faces-redirect=true");
    }

    public void carregarClientes(){
        cliente = Faces.getFlashAttribute("Cliente");


        if(cliente == null){
            Faces.navigate("cliente-listar.xhtml?faces-redirect=true");
        }
    }

    public void salvarCadastro(){
            try{
                if(cliente.getUSR_ID() == null){
                    org.apache.http.client.HttpClient httpclient = HttpClients.createDefault();
                    HttpPost httppost = new HttpPost("http://localhost:3000/cliente/cadastro");


                    List<NameValuePair> params = new ArrayList<>(2);
                    params.add(new BasicNameValuePair("USR_EMAIL", cliente.getUSR_EMAIL()));
                    params.add(new BasicNameValuePair("USR_SENHA", cliente.getUSR_SENHA()));
                    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                    HttpResponse response = httpclient.execute(httppost);

                }else{
                    HttpClient httpclient = HttpClients.createDefault();
                    HttpPost httppost = new HttpPost("http://localhost:3000/cliente/update");


                    List<NameValuePair> params = new ArrayList<NameValuePair>(3);
                    params.add(new BasicNameValuePair("USR_ID", cliente.getUSR_ID().toString()));
                    params.add(new BasicNameValuePair("USR_EMAIL", cliente.getUSR_EMAIL()));
                    params.add(new BasicNameValuePair("USR_SENHA", cliente.getUSR_SENHA()));
                    httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

                    HttpResponse response = httpclient.execute(httppost);
                }

                Messages.addFlashGlobalInfo("Cliente salvo com sucesso!");

                Faces.navigate("cliente-listar.xhtml?faces-redirect=true");

            }

            catch(Exception excecao){
                Messages.addGlobalError("Erro!");
            }
    }

    public void excluir(){
        try{
            org.apache.http.client.HttpClient httpclient = HttpClients.createDefault();
            HttpPost httppost = new HttpPost("http://localhost:3000/cliente/delete");


            List<NameValuePair> params = new ArrayList<>(1);
            params.add(new BasicNameValuePair("USR_ID", cliente.getUSR_ID().toString()));
            httppost.setEntity(new UrlEncodedFormEntity(params, "UTF-8"));

            HttpResponse response = httpclient.execute(httppost);


            Messages.addFlashGlobalInfo("Cliente excluído com sucesso!");

            Faces.navigate("cliente-listar.xhtml?faces-redirect=true");
        }catch (Exception excecao){
            Messages.addGlobalError("O cliente que deseja remover está vinculado com outros registros!");
        }
    }

    public void selecionarExclusao(Cliente cliente){
        Faces.setFlashAttribute("Cliente", cliente);
        Faces.navigate("cliente-exclusao.xhtml?faces-redirect=true");
    }

}
