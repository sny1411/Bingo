/**
 * Precompiled [bingo.run-clients.gradle.kts][Bingo_run_clients_gradle] script plugin.
 *
 * @see Bingo_run_clients_gradle
 */
public
class Bingo_runClientsPlugin : org.gradle.api.Plugin<org.gradle.api.Project> {
    override fun apply(target: org.gradle.api.Project) {
        try {
            Class
                .forName("Bingo_run_clients_gradle")
                .getDeclaredConstructor(org.gradle.api.Project::class.java, org.gradle.api.Project::class.java)
                .newInstance(target, target)
        } catch (e: java.lang.reflect.InvocationTargetException) {
            throw e.targetException
        }
    }
}
