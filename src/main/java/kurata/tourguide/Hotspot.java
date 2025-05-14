package kurata.tourguide;

import java.util.List;

public class Hotspot {
    private final String id;
    private final String name;
    private final String audioPath;
    private final String videoPath;
    private final String imagePath;  // Add imagePath
    private final String description;
    private final List<Quiz> quizzes;

    public Hotspot(String id, String name, String audioPath, String videoPath, String imagePath, String description, List<Quiz> quizzes) {
        this.id = id;
        this.name = name;
        this.audioPath = audioPath;
        this.videoPath = videoPath;
        this.imagePath = imagePath;  // Initialize imagePath
        this.description = description;
        this.quizzes = quizzes;
    }

    // Getter for imagePath
    public String getImagePath() {
        return imagePath;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getAudioPath() { return audioPath; }
    public String getVideoPath() { return videoPath; }
    public String getDescription() { return description; }
    public List<Quiz> getQuizzes() { return quizzes; }

    public static Hotspot fromId(String id) {
        switch (id) {
            case "katse":
                return new Hotspot("katse", "Katse Dam",
                        "katse.mp3", "katse.mp4", "katse.jpg",  // Add image path here
                        "Katse Dam is a major dam in Lesotho.",
                        List.of(
                                new Quiz("What river does Katse Dam block?",
                                        new String[] {"Senqu", "Caledon", "Mohokare", "Makhaleng"}, 0),
                                new Quiz("What is the main purpose of Katse Dam?",
                                        new String[] {"Irrigation", "Flood control", "Hydropower & water transfer", "Fishing"}, 2)
                        )
                );

            case "letseng-mine":
                return new Hotspot("letseng-mine", "Lets'eng Mine",
                        "letsheng.mp3", "letsheng.mp4", "letsheng.jpg",  // Add image path here
                        "Lets'eng Mine is one of the world's highest diamond-producing mines.",
                        List.of(
                                new Quiz("What is Lets'eng Mine known for?",
                                        new String[] {"Gold mining", "Diamond mining", "Coal mining", "Iron ore mining"}, 1),
                                new Quiz("What country is Lets'eng Mine located in?",
                                        new String[] {"South Africa", "Lesotho", "Botswana", "Namibia"}, 1)
                        )
                );

            case "pioneer-mall":
                return new Hotspot("pioneer-mall", "Pioneer Mall",
                        "pioneer.mp3", "pioneer.mp4", "pioneer.jpg",  // Add image path here
                        "Pioneer Mall is a major shopping center in Maseru.",
                        List.of(
                                new Quiz("What is located near Pioneer Mall?",
                                        new String[] {"A university", "A park", "A bus terminal", "A hospital"}, 2),
                                new Quiz("What is the main attraction of Pioneer Mall?",
                                        new String[] {"Shopping", "Cultural heritage", "Tourist attractions", "Restaurants"}, 0)
                        )
                );
            default:
                return null;
        }
    }

    public static class Quiz {
        private final String question;
        private final String[] choices;
        private final int correctIndex;

        public Quiz(String question, String[] choices, int correctIndex) {
            this.question = question;
            this.choices = choices;
            this.correctIndex = correctIndex;
        }

        public String getQuestion() { return question; }
        public String[] getChoices() { return choices; }
        public int getCorrectIndex() { return correctIndex; }
    }
}
