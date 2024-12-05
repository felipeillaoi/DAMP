public class Login {
    private String login;
    private String senha;

    public Login() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public void registrar(String login, String senha) {
        this.login = login;
        this.senha = senha;
        System.out.println("Registro concluído com sucesso.");
    }

    public boolean validar(String login, String senha) {
        if (this.login.equals(login) && this.senha.equals(senha)) {
            System.out.println("Login bem-sucedido!");
            return true;
        } else {
            System.out.println("Login ou senha inválidos.");
            return false;
        }
    }
}
