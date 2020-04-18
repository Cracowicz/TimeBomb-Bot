import net.dv8tion.jda.api.AccountType;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.security.auth.login.LoginException;

public class Main extends ListenerAdapter {
    public static void main(String[] args) throws LoginException {
        JDABuilder builder = new JDABuilder(AccountType.BOT);
        String token = "Njk4NTU2MjgzMDY5OTIzMzI4.XpI3ww.3jtGywl9WMtXyrdLB66v7rcM2BM";
        builder.setToken(token);
        builder.addEventListeners(new EventListener());
        JDA jda = builder.build();
    }
}
