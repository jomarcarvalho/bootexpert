package vendas2.dao;

import java.text.Normalizer;
import java.util.List;
import java.util.regex.Pattern;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.transaction.Transactional;

import org.slf4j.ILoggerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import vendas2.exception.RegraNegocioException;
import vendas2.modelo.Cliente;

@Repository
public class ClienteDAO {

    private static final Logger logger = LoggerFactory.getLogger(ClienteDAO.class);
    @Autowired
    private EntityManager em;

    /**
     * Substitui letras acentuadas para padrao ascii
     *
     * @param str
     * @return texto
     * @code util
     */
    private String deAccent(String str) {
        String nfdNormalizedString = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(nfdNormalizedString).replaceAll("");
    }

    /**
     * - o rollback da transação depende diretamente da forma como o Spring gerencia a anotação
     * Transactional, ou seja, se a exception for tratada dentro do metodo anotado com
     * Transactional o rollback nao ocorre, mas se a exception for propagada para fora do
     * metodo então o rollback ocorre.<br>
     * - Se a exceção for checada (subclasse de Exception que não é RuntimeException), o
     * rollback não acontece automaticamente, a menos que você configure explicitamente
     * usando o comando "throw e" dentro do catch.<br>
     * - Mas no metodo abaixo, você está capturando a exceção dentro do próprio metodo (catch
     * (Exception e)), e não deixando ela "subir" para o Spring e como resultado, o Spring
     * não vê a exceção e não faz rollback, porque do ponto de vista da transação, o
     * metodo terminou normalmente retornando null.<br>
     *
     * @param cliente
     * @return inteiro
     */
    @Transactional
    public Integer testeinserirClienteTratado(Cliente cliente) {
        try {
            logger.debug("[testeinserirClienteTratado] gerando exception desejada");
            System.out.println("[testeinserirClienteTratado] gerando exception desejada");
            throw new RegraNegocioException("[testeinserirClienteTratado] exception desejada");
        } catch (Exception e) {
            logger.error("[testeinserirClienteTratado] listando exception gerada");
            System.out.println("[testeinserirClienteTratado] listando exception gerada");
            logger.error(e.getMessage(), e);
            // a linha abaixo gera exception nao checada para forçar rollback
            //throw e;
        }
        logger.error("[testeinserirClienteTratado] retornando null");
        System.out.println("[testeinserirClienteTratado] retornando null");
        return null;
    }

    /**
     * a exception nao e checada deixando-a propagar para fora do metodo, o que
     * faz com que o Spring faça rollback da transação, ou seja, o cliente
     * nao e inserido na base de dados.<br>
     * Acompanhe o resultado na classe AdviceController.<br>
     * @param cliente
     * @return
     */
    @Transactional
    public Integer testeinserirClienteNaoTratado(Cliente cliente) {
        logger.debug("[testeinserirClienteNaoTratado] gerando erro desejado");
        System.out.println("[testeinserirClienteNaoTratado] gerando exception desejada");
        throw new RegraNegocioException("[testeinserirClienteNaoTratado] erro desejado");
    }

    /**
     * Insere cliente na base
     *
     * @param cliente Objeto Cliente
     * @return identificador do registro inserido na base
     * @code util
     */
    @Transactional
    public Integer inserirCliente(Cliente cliente) {
        cliente.setNome(deAccent(cliente.getNome()));
        em.persist(cliente);
        return cliente.getId();
    }

    /**
     * Insere um lote de clientes na base
     *
     * @param cliente lista de Cliente
     * @return Quantidade de registros inseridos na base
     * @code util
     */
    @Transactional
    public Integer inserirLote(List<Cliente> cliente) {
        try {
            Integer qtd = 0;
            for (Cliente c : cliente) {
                c.setNome(deAccent(c.getNome()));
                em.persist(c);
                qtd += 1;
            }
            return qtd;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Atualiza registro de um cliente na base
     *
     * @return Object cliente
     * @code util
     */
    @Transactional
    public Cliente atualizarClienteByCliente(Cliente cliente) {
        try {
            cliente.setNome(deAccent(cliente.getNome()));
            em.merge(cliente);
            return cliente;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Atualiza lote de clientes na base
     *
     * @param cliente lista de cliente
     * @return quantidade de registros atualizados
     * @code util
     */
    @Transactional
    public Integer atualizarLoteDeCliente(List<Cliente> cliente) {
        try {
            Integer qtd = 0;
            for (Cliente c : cliente) {
                c.setNome(deAccent(c.getNome()));
                em.merge(c);
                qtd += 1;
            }
            return qtd;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }


    /**
     * Exclui um cliente da base
     *
     * @param cliente Objeto cliente
     * @return Object cliente
     * @code util
     */
    @Transactional
    public Cliente excluirClienteByCliente(Cliente cliente) {
        try {
            if (!em.contains(cliente)) {
                em.merge(cliente);
            }
            em.remove(cliente);
            return cliente;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Remove um cliente da base
     *
     * @param id numero do cliente
     * @return Object cliente
     * @code util
     */
    @Transactional
    public Cliente excluirClienteByClienteId(Integer id) {
        try {
            Cliente cliente = em.find(Cliente.class, id);
            excluirClienteByCliente(cliente);
            return cliente;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Exclui todos os registros da tabela cliente
     *
     * @return int total de registros excluidos
     * @code util
     */
    @Transactional
    public int excluirTodosCliente() {
        try {
            List<Cliente> l = obtemTodosClientes();
            for (Cliente c : l) {
                if (!em.contains(c)) {
                    em.merge(c);
                }
                em.remove(c);
            }
            return l.size();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Busca clientes onde o campo nome contenha o texto passado no parametro
     *
     * @param nome alguma coisa que se pareça com o nome do cliente
     * @return lista de clientes
     * @code util
     */
    public List<Cliente> obtemClienteByNomeLike(String nome) {
        try {
            String hql = "select c from Cliente c where lower(c.nome) like lower(concat('%', :umnome, '%')) ";
            TypedQuery<Cliente> query = em.createQuery(hql, Cliente.class);
            query.setParameter("umnome", nome);
            List<Cliente> x = query.getResultList();
            return x;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Busca cliente
     *
     * @param txtcpf cpf do cliente
     * @return Cliente
     * @code util
     */
    public Cliente obtemClienteByCpf(String txtcpf) {
        try {
            String hql = "select c from Cliente c where c.cpf = :cpf";
            TypedQuery<Cliente> query = em.createQuery(hql, Cliente.class);
            query.setParameter("cpf", txtcpf);
            List<Cliente> lqry = query.getResultList();
            Cliente qry = null;
            if (lqry.size() > 0) {
                qry = lqry.get(0);
                return qry;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Obtem cliente
     *
     * @param id numero do cliente
     * @return Cliente
     * @code util
     */
    public Cliente obtemClienteByClienteId(Integer id) {
        try {
            Cliente c = em.find(Cliente.class, id);
            if (c != null) {
                return c;
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Consulta todos os clientes cadastrados
     *
     * @return lista de clientes
     * @code util
     */
    public List<Cliente> obtemTodosClientes() {
        try {
            String hql = "select c from Cliente c order by c.id desc";
            List<Cliente> resultList = em.createQuery(hql, Cliente.class).getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Consulta todos os clientes que tenham pedido cadastrado
     *
     * @return lista Cliente
     * @code util
     */
    public List<Cliente> obtemClientesComPedido() {
        try {
            String hql = "select c from Cliente c where c.id in "
                    + "(select d.id from Cliente d join Pedido p on d.id = p.cliente) order by c.id asc";
            List<Cliente> resultList = em.createQuery(hql, Cliente.class).getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }


    /**
     * Consulta todos os clientes que nao tenham pedido cadastrado
     *
     * @return lista Cliente
     * @code util
     */
    public List<Cliente> obtemClientesSemPedido() {
        try {
            String hql = "select c from Cliente c where c.id not in "
                    + "(select d.id from Cliente d join Pedido p on d.id = p.cliente) order by c.id asc";
            List<Cliente> resultList = em.createQuery(hql, Cliente.class).getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Consulta todos os clientes que nao tenham pedido cadastrado e
     * que o campo nome contenha o texto case insensitive passado como parametro
     *
     * @param nome algum texto que se pareça com o nome do cliente
     * @return lista Cliente
     * @code util
     */
    public List<Cliente> obtemClientesSemPedidoNomeLike(String nome) {
        try {
            String hql = "select c from Cliente c where c.id not in (select d.id from Cliente d join "
                    + "Pedido p on d.id = p.cliente) and lower(c.nome) "
                    + "like lower(concat('%', :nameToFind,'%')) order by c.id asc";
            TypedQuery<Cliente> clientessempedidonomelike = em.createQuery(hql, Cliente.class);
            clientessempedidonomelike.setParameter("nameToFind", nome);
            List<Cliente> resultList = clientessempedidonomelike.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

    /**
     * Consulta todos os clientes que tenham pedido cadastrado e
     * que o campo nome contenha o texto case insensitive passado como parametro
     *
     * @param nome algum texto que se pareça com o nome do cliente
     * @return lista Cliente
     * @code util
     */
    public List<Cliente> obtemClientesComPedidoNomeLike(String nome) {
        try {
            String hql = "select c from Cliente c where c.id in (select d.id from Cliente d join "
                    + "Pedido p on d.id = p.cliente) and lower(c.nome) "
                    + "like lower(concat('%', :nameToFind,'%')) order by c.id asc";
            TypedQuery<Cliente> clientescompedidonomelike = em.createQuery(hql, Cliente.class);
            clientescompedidonomelike.setParameter("nameToFind", nome);
            List<Cliente> resultList = clientescompedidonomelike.getResultList();
            return resultList;
        } catch (Exception e) {
            e.printStackTrace();
            throw new RegraNegocioException(e.getMessage(), e);
        }
    }

}
