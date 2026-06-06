package br.com.fiap.smartdisaster.config;

import br.com.fiap.smartdisaster.entity.*;
import br.com.fiap.smartdisaster.enums.StatusAbrigo;
import br.com.fiap.smartdisaster.enums.StatusDoacao;
import br.com.fiap.smartdisaster.enums.StatusNecessidade;
import br.com.fiap.smartdisaster.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataLoader {

    private final UsuarioRepository usuarioRepository;
    private final VoluntarioRepository voluntarioRepository;
    private final AbrigoRepository abrigoRepository;
    private final VitimaRepository vitimaRepository;
    private final DoacaoRepository doacaoRepository;
    private final NecessidadeRepository necessidadeRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedDatabase() {
        return args -> {
            if (usuarioRepository.count() > 0) {
                log.info("Banco já populado — seed ignorado");
                return;
            }

            // ── Usuários ──────────────────────────────────────────────────────────
            Admin admin = new Admin();
            admin.setNome("Administrador SmartDisaster");
            admin.setEmail("admin@smartdisaster.com");
            admin.setSenha(passwordEncoder.encode("123456"));
            usuarioRepository.save(admin);

            Voluntario vol1 = new Voluntario();
            vol1.setNome("Carlos Mendes");
            vol1.setEmail("voluntario@smartdisaster.com");
            vol1.setSenha(passwordEncoder.encode("123456"));
            vol1.setTelefone("(11) 98765-4321");
            voluntarioRepository.save(vol1);

            Voluntario vol2 = new Voluntario();
            vol2.setNome("Ana Lima");
            vol2.setEmail("ana@smartdisaster.com");
            vol2.setSenha(passwordEncoder.encode("vol123"));
            vol2.setTelefone("(11) 91234-5678");
            voluntarioRepository.save(vol2);

            // ── Abrigos ───────────────────────────────────────────────────────────
            Abrigo abrigo1 = new Abrigo();
            abrigo1.setNome("Abrigo Central São Paulo");
            abrigo1.setCapacidadeMaxima(200);
            abrigo1.setStatus(StatusAbrigo.ATIVO);
            abrigo1.setLatitude(-23.5505);
            abrigo1.setLongitude(-46.6333);
            abrigo1.setEndereco(Endereco.builder()
                    .rua("Av. Paulista").numero("1000")
                    .bairro("Bela Vista").cidade("São Paulo")
                    .estado("SP").cep("01310-100").build());
            abrigoRepository.save(abrigo1);

            Abrigo abrigo2 = new Abrigo();
            abrigo2.setNome("Ginásio Municipal de Guarulhos");
            abrigo2.setCapacidadeMaxima(500);
            abrigo2.setStatus(StatusAbrigo.LOTADO);
            abrigo2.setLatitude(-23.4543);
            abrigo2.setLongitude(-46.5338);
            abrigo2.setEndereco(Endereco.builder()
                    .rua("Rua das Flores").numero("45")
                    .bairro("Centro").cidade("Guarulhos")
                    .estado("SP").cep("07010-000").build());
            abrigoRepository.save(abrigo2);

            Abrigo abrigo3 = new Abrigo();
            abrigo3.setNome("Escola Estadual de Osasco");
            abrigo3.setCapacidadeMaxima(150);
            abrigo3.setStatus(StatusAbrigo.ATIVO);
            abrigo3.setLatitude(-23.5324);
            abrigo3.setLongitude(-46.7924);
            abrigo3.setEndereco(Endereco.builder()
                    .rua("Rua Deputado Emílio Carlos").numero("300")
                    .bairro("Km 18").cidade("Osasco")
                    .estado("SP").cep("06210-001").build());
            abrigoRepository.save(abrigo3);

            Abrigo abrigo4 = new Abrigo();
            abrigo4.setNome("Ginásio Poliesportivo de Porto Alegre");
            abrigo4.setCapacidadeMaxima(800);
            abrigo4.setStatus(StatusAbrigo.LOTADO);
            abrigo4.setLatitude(-30.0346);
            abrigo4.setLongitude(-51.2177);
            abrigo4.setEndereco(Endereco.builder()
                    .rua("Av. Bento Gonçalves").numero("2500")
                    .bairro("Partenon").cidade("Porto Alegre")
                    .estado("RS").cep("90650-003").build());
            abrigoRepository.save(abrigo4);

            Abrigo abrigo5 = new Abrigo();
            abrigo5.setNome("Centro Comunitário Canoas");
            abrigo5.setCapacidadeMaxima(350);
            abrigo5.setStatus(StatusAbrigo.ATIVO);
            abrigo5.setLatitude(-29.9178);
            abrigo5.setLongitude(-51.1794);
            abrigo5.setEndereco(Endereco.builder()
                    .rua("Rua Tiradentes").numero("180")
                    .bairro("Centro").cidade("Canoas")
                    .estado("RS").cep("92010-270").build());
            abrigoRepository.save(abrigo5);

            Abrigo abrigo6 = new Abrigo();
            abrigo6.setNome("Escola Municipal Visconde de Mauá");
            abrigo6.setCapacidadeMaxima(250);
            abrigo6.setStatus(StatusAbrigo.ATIVO);
            abrigo6.setLatitude(-22.9068);
            abrigo6.setLongitude(-43.1729);
            abrigo6.setEndereco(Endereco.builder()
                    .rua("Rua Visconde de Mauá").numero("120")
                    .bairro("Santa Teresa").cidade("Rio de Janeiro")
                    .estado("RJ").cep("20240-900").build());
            abrigoRepository.save(abrigo6);

            // ── Vítimas ───────────────────────────────────────────────────────────
            Vitima vitima1 = new Vitima();
            vitima1.setNome("João da Silva");
            vitima1.setCpf("123.456.789-00");
            vitima1.setDataNascimento(LocalDate.of(1985, 3, 15));
            vitima1.setCondicaoSaude("Hipertensão leve");
            vitima1.setAbrigo(abrigo1);
            vitimaRepository.save(vitima1);

            Vitima vitima2 = new Vitima();
            vitima2.setNome("Maria Oliveira");
            vitima2.setCpf("987.654.321-00");
            vitima2.setDataNascimento(LocalDate.of(1990, 7, 22));
            vitima2.setCondicaoSaude("Sem condições especiais");
            vitima2.setAbrigo(abrigo1);
            vitimaRepository.save(vitima2);

            Vitima vitima3 = new Vitima();
            vitima3.setNome("Pedro Santos");
            vitima3.setCpf("111.222.333-44");
            vitima3.setDataNascimento(LocalDate.of(1975, 11, 8));
            vitima3.setCondicaoSaude("Diabetes — necessita insulina");
            vitima3.setAbrigo(abrigo2);
            vitimaRepository.save(vitima3);

            // ── Necessidades (3 por abrigo) ───────────────────────────────────────
            // Abrigo 1
            Necessidade nec1a = new Necessidade();
            nec1a.setTipo("Alimentos");
            nec1a.setDescricao("Alimentos não perecíveis para 3 dias");
            nec1a.setQuantidadeNecessaria(200);
            nec1a.setStatus(StatusNecessidade.PENDENTE);
            nec1a.setAbrigo(abrigo1);
            necessidadeRepository.save(nec1a);

            Necessidade nec1b = new Necessidade();
            nec1b.setTipo("Água");
            nec1b.setDescricao("Garrafas de água potável 500ml");
            nec1b.setQuantidadeNecessaria(500);
            nec1b.setStatus(StatusNecessidade.PENDENTE);
            nec1b.setAbrigo(abrigo1);
            necessidadeRepository.save(nec1b);

            Necessidade nec1c = new Necessidade();
            nec1c.setTipo("Higiene");
            nec1c.setDescricao("Kits de higiene pessoal (sabonete, escova, pasta)");
            nec1c.setQuantidadeNecessaria(100);
            nec1c.setStatus(StatusNecessidade.PENDENTE);
            nec1c.setAbrigo(abrigo1);
            necessidadeRepository.save(nec1c);

            // Abrigo 2
            Necessidade nec2a = new Necessidade();
            nec2a.setTipo("Medicamentos");
            nec2a.setDescricao("Insulina para paciente diabético");
            nec2a.setQuantidadeNecessaria(5);
            nec2a.setStatus(StatusNecessidade.PENDENTE);
            nec2a.setAbrigo(abrigo2);
            necessidadeRepository.save(nec2a);

            Necessidade nec2b = new Necessidade();
            nec2b.setTipo("Cobertores");
            nec2b.setDescricao("Cobertores para o frio da madrugada");
            nec2b.setQuantidadeNecessaria(80);
            nec2b.setStatus(StatusNecessidade.PENDENTE);
            nec2b.setAbrigo(abrigo2);
            necessidadeRepository.save(nec2b);

            Necessidade nec2c = new Necessidade();
            nec2c.setTipo("Roupas");
            nec2c.setDescricao("Roupas de inverno adulto e infantil");
            nec2c.setQuantidadeNecessaria(120);
            nec2c.setStatus(StatusNecessidade.PENDENTE);
            nec2c.setAbrigo(abrigo2);
            necessidadeRepository.save(nec2c);

            // Abrigo 3
            Necessidade nec3a = new Necessidade();
            nec3a.setTipo("Alimentos");
            nec3a.setDescricao("Leite e alimentos para crianças");
            nec3a.setQuantidadeNecessaria(150);
            nec3a.setStatus(StatusNecessidade.PENDENTE);
            nec3a.setAbrigo(abrigo3);
            necessidadeRepository.save(nec3a);

            Necessidade nec3b = new Necessidade();
            nec3b.setTipo("Higiene");
            nec3b.setDescricao("Fraldas descartáveis tamanho M e G");
            nec3b.setQuantidadeNecessaria(300);
            nec3b.setStatus(StatusNecessidade.ATENDIDA);
            nec3b.setAbrigo(abrigo3);
            necessidadeRepository.save(nec3b);

            Necessidade nec3c = new Necessidade();
            nec3c.setTipo("Medicamentos");
            nec3c.setDescricao("Antitérmicos e analgésicos básicos");
            nec3c.setQuantidadeNecessaria(50);
            nec3c.setStatus(StatusNecessidade.PENDENTE);
            nec3c.setAbrigo(abrigo3);
            necessidadeRepository.save(nec3c);

            // ── Doações de exemplo ────────────────────────────────────────────────
            Doacao doacao1 = new Doacao();
            doacao1.setTipo("Alimentos");
            doacao1.setDescricao("Caixas de arroz, feijão e macarrão");
            doacao1.setQuantidade(50);
            doacao1.setDataDoacao(LocalDate.now().minusDays(2));
            doacao1.setStatus(StatusDoacao.PENDENTE_ENTREGA);
            doacao1.setVoluntario(vol1);
            doacao1.setAbrigo(abrigo1);
            doacao1.setNecessidade(nec1a);
            doacaoRepository.save(doacao1);

            Doacao doacao2 = new Doacao();
            doacao2.setTipo("Água");
            doacao2.setDescricao("Galões de água potável 20L");
            doacao2.setQuantidade(30);
            doacao2.setDataDoacao(LocalDate.now().minusDays(5));
            doacao2.setStatus(StatusDoacao.ENTREGUE);
            doacao2.setVoluntario(vol1);
            doacao2.setAbrigo(abrigo1);
            doacao2.setNecessidade(nec1b);
            doacaoRepository.save(doacao2);

            Doacao doacao3 = new Doacao();
            doacao3.setTipo("Medicamentos");
            doacao3.setDescricao("Insulina — 10 unidades");
            doacao3.setQuantidade(10);
            doacao3.setDataDoacao(LocalDate.now().minusDays(1));
            doacao3.setStatus(StatusDoacao.PENDENTE_ENTREGA);
            doacao3.setVoluntario(vol2);
            doacao3.setAbrigo(abrigo2);
            doacao3.setNecessidade(nec2a);
            doacaoRepository.save(doacao3);

            Doacao doacao4 = new Doacao();
            doacao4.setTipo("Cobertores");
            doacao4.setDescricao("Cobertores térmicos adulto");
            doacao4.setQuantidade(20);
            doacao4.setDataDoacao(LocalDate.now().minusDays(3));
            doacao4.setStatus(StatusDoacao.CANCELADA);
            doacao4.setVoluntario(vol2);
            doacao4.setAbrigo(abrigo2);
            doacao4.setNecessidade(nec2b);
            doacaoRepository.save(doacao4);

            log.info("Seed concluído: 1 admin, 2 voluntários, 6 abrigos, 3 vítimas, 9 necessidades, 4 doações");
        };
    }
}
