import java.lang.Thread;
import java.lang.Runnable;
import java.util.Random; // Importação necessária para tempo aleatório

public class SimpleConcurrentSolutionV2 {

    // 1.2. Tarefa de Verificação de Recursos (Classe Interna Não Anônima com TEMPO ALEATÓRIO)
    private static class ResourceCheckTask implements Runnable {
        
        private static final Random random = new Random();

        @Override
        public void run() {
            Thread currentThread = Thread.currentThread();

            System.out.println("[" + currentThread.getName() + "] INÍCIO: Verificação de Recursos.");
                try {
                    // Gera um valor aleatório entre 1000ms (1s) e 3000ms (3s)
                    int sleepTime = 1000 + random.nextInt(2000); 
                    System.out.println("[" + currentThread.getName() + "] Verificando Recurso " + " (Duração: " + sleepTime + "ms)...");
                    Thread.sleep(sleepTime); 
                } catch (InterruptedException e) {
                    System.err.println("[" + currentThread.getName() + "] Verificação interrompida.");
                    Thread.currentThread().interrupt();
                    return;
                }
            System.out.println("[" + currentThread.getName() + "] FIM: Verificação concluída.");
        }
    }
    // Fim da Classe Interna não anônima

    // 1.1. Tarefa de Inicialização de Logs (Tempo Fixo)
    private static Runnable logSetupTask = new Runnable() {
        @Override
        public void run() {
            Thread currentThread = Thread.currentThread();

            System.out.println("[" + currentThread.getName() + "] INÍCIO: Configuração de Logs...");

            try {
                Thread.sleep(4000); // TEMPO FIXO: 4.0 segundos
            } catch (InterruptedException e) {
                System.err.println("[" + currentThread.getName() + "] Configuração de Logs interrompida.");
                Thread.currentThread().interrupt();
                return;
            }

            System.out.println("[" + currentThread.getName() + "] FIM: Configuração de Logs concluída.");
        }
    };

    public static void main(String[] args) {
        Thread currentThread = Thread.currentThread();
        System.out.println("[" + currentThread.getName() + "] --- INÍCIO DO PROGRAMA JAVA ---");

        // Criação das instâncias
        Thread tLogs = new Thread(logSetupTask, "Setup-Logs");
        tLogs.start();
        System.out.println("[" + currentThread.getName() + "] Inicialização de tarefas concorrentes solicitada.");
        for (int i = 1; i <= 5; i++) {
            ResourceCheckTask checkInstance = new ResourceCheckTask(); 
            // Criação e Início das Threads
            Thread tCheck = new Thread(checkInstance, "Check-Recursos" + i);
            // Início da execução concorrente
            tCheck.start();
            
            try {
                tLogs.join();
                tCheck.join();
            } catch(InterruptedException e){
                Thread.currentThread().interrupt();
                return;
            }
        }

        System.err.println("[" + currentThread.getName() + "] interrompida.");
        System.out.println("[" + currentThread.getName() + "] --- FIM DO PROGRAMA JAVA (Main terminou) ---");
    }
}
