package tw.edu.yuntech.app;

class Info {
    private String cell;
    private String username;
    private String pwd;

    public Info() {}

    public Info(String cell, String username, String pwd) {
        this.cell =cell;
        this.username =username;
        this.pwd = pwd;
    }

    public String getCell() {
        return cell;
    }

    public String getUsername() {
        return username;
    }

    public String getPwd() {
        return pwd;
    }
}
