import java.util.ArrayList;
import java.util.List;

public class Main {

    // ПАТТЕРН «ФАСАД»
    static class TV {
        public void on() { System.out.println("Телевизор включён"); }
        public void off() { System.out.println("Телевизор выключен"); }
        public void setInput(String input) { System.out.println("Телевизор: вход переключён на " + input); }
    }

    static class AudioSystem {
        public void on() { System.out.println("Аудиосистема включена"); }
        public void off() { System.out.println("Аудиосистема выключена"); }
        public void setVolume(int level) { System.out.println("Громкость аудиосистемы: " + level); }
    }

    static class DVDPlayer {
        public void play() { System.out.println("DVD проигрыватель: воспроизведение"); }
        public void pause() { System.out.println("DVD проигрыватель: пауза"); }
        public void stop() { System.out.println("DVD проигрыватель: остановка"); }
    }

    static class GameConsole {
        public void on() { System.out.println("Игровая консоль включена"); }
        public void startGame(String gameName) { System.out.println("Запуск игры: " + gameName); }
    }

    // ----- Фасад -----
    static class HomeTheaterFacade {
        private TV tv;
        private AudioSystem audio;
        private DVDPlayer dvd;
        private GameConsole gameConsole;

        public HomeTheaterFacade(TV tv, AudioSystem audio, DVDPlayer dvd, GameConsole gameConsole) {
            this.tv = tv;
            this.audio = audio;
            this.dvd = dvd;
            this.gameConsole = gameConsole;
        }

        public void watchMovie() {
            System.out.println("\n=== Начинаем просмотр фильма ===");
            tv.on();
            tv.setInput("HDMI");
            audio.on();
            audio.setVolume(15);
            dvd.play();
        }

        public void playGame(String gameName) {
            System.out.println("\n=== Запускаем игру ===");
            tv.on();
            tv.setInput("HDMI");
            audio.on();
            audio.setVolume(20);
            gameConsole.on();
            gameConsole.startGame(gameName);
        }

        public void listenMusic() {
            System.out.println("\n=== Прослушивание музыки ===");
            tv.on();
            tv.setInput("AUX");
            audio.on();
            audio.setVolume(10);
        }

        public void turnOff() {
            System.out.println("\n=== Выключение системы ===");
            dvd.stop();
            audio.off();
            tv.off();
        }

        public void setVolume(int level) {
            audio.setVolume(level);
        }
    }

    //ЧАСТЬ 2: ПАТТЕРН «КОМПОНОВЩИК»
    static abstract class FileSystemComponent {
        protected String name;

        public FileSystemComponent(String name) {
            this.name = name;
        }

        public abstract void display(String indent);
        public abstract int getSize();

        public void add(FileSystemComponent component) {
            throw new UnsupportedOperationException();
        }

        public void remove(FileSystemComponent component) {
            throw new UnsupportedOperationException();
        }
    }

    static class File extends FileSystemComponent {
        private int size;

        public File(String name, int size) {
            super(name);
            this.size = size;
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "📄 " + name + " (" + size + " KB)");
        }

        @Override
        public int getSize() {
            return size;
        }
    }

    static class Directory extends FileSystemComponent {
        private List<FileSystemComponent> children = new ArrayList<>();

        public Directory(String name) {
            super(name);
        }

        @Override
        public void add(FileSystemComponent component) {
            if (component != null && !children.contains(component)) {
                children.add(component);
            }
        }

        @Override
        public void remove(FileSystemComponent component) {
            children.remove(component);
        }

        @Override
        public void display(String indent) {
            System.out.println(indent + "📁 " + name);
            for (FileSystemComponent child : children) {
                child.display(indent + "  ");
            }
        }

        @Override
        public int getSize() {
            int total = 0;
            for (FileSystemComponent child : children) {
                total += child.getSize();
            }
            return total;
        }
    }

    public static void main(String[] args) {
        
        System.out.println("========== ДЕМОНСТРАЦИЯ ПАТТЕРНА «ФАСАД» ==========");

        TV tv = new TV();
        AudioSystem audio = new AudioSystem();
        DVDPlayer dvd = new DVDPlayer();
        GameConsole gameConsole = new GameConsole();

        HomeTheaterFacade homeTheater = new HomeTheaterFacade(tv, audio, dvd, gameConsole);

        homeTheater.watchMovie();
        homeTheater.setVolume(12);
        homeTheater.playGame("Cyberpunk 2077");
        homeTheater.listenMusic();
        homeTheater.turnOff();

        System.out.println("\n\n========== ДЕМОНСТРАЦИЯ ПАТТЕРНА «КОМПОНОВЩИК» ==========");

        File file1 = new File("resume.pdf", 120);
        File file2 = new File("photo.jpg", 350);
        File file3 = new File("movie.mp4", 2048);
        File file4 = new File("notes.txt", 5);

        Directory root = new Directory("root");
        Directory documents = new Directory("Documents");
        Directory media = new Directory("Media");
        Directory videos = new Directory("Videos");

        root.add(documents);
        root.add(media);

        documents.add(file1);
        documents.add(file4);

        media.add(file2);
        media.add(videos);

        videos.add(file3);

        System.out.println("=== Файловая система ===");
        root.display("");

        System.out.println("\nОбщий размер root: " + root.getSize() + " KB");
    }
}