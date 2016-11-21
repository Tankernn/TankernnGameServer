package eu.tankernn.game.server;

public class Launcher {
	public static void main(String[] args) {
		int port;
        if (args.length > 0) {
            port = Integer.parseInt(args[0]);
        } else {
            port = 8080;
        }
        try {
			new GameServer(port).run();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
