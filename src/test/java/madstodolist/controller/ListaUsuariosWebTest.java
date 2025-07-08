package madstodolist.controller;

import madstodolist.dto.UsuarioData;
import madstodolist.service.UsuarioService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UsuarioController.class)
public class ListaUsuariosWebTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UsuarioService usuarioService;

    @Test
    @WithMockUser(username = "test@example.com", password = "password", roles = "USER")
    public void testListarUsuariosDevuelveVistaConUsuarios() throws Exception {
        // 1. Configurar datos de prueba
        UsuarioData user1 = new UsuarioData(1L, "user1@example.com");
        UsuarioData user2 = new UsuarioData(2L, "user2@example.com");
        List<UsuarioData> usuarios = Arrays.asList(user1, user2);

        // Simular usuario logeado
        UsuarioData usuarioLogeado = new UsuarioData(3L, "test@example.com");
        
        // 2. Configurar mocks
        Mockito.when(usuarioService.allUsuarios()).thenReturn(usuarios);
        Mockito.when(usuarioService.findByEmail("test@example.com")).thenReturn(usuarioLogeado);

        // 3. Ejecutar prueba y verificar resultados
        mockMvc.perform(get("/registrados"))
                .andExpect(status().isOk())
                .andExpect(view().name("listaUsuarios"))
                .andExpect(model().attributeExists("usuarios"))
                .andExpect(model().attribute("usuarios", usuarios))
                .andExpect(model().attributeExists("usuarioLogeado"))
                .andExpect(model().attribute("usuarioLogeado", usuarioLogeado));
    }
}
