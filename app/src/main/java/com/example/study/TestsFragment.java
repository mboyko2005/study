package com.example.study;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.study.database.AppDatabase;
import com.example.study.database.CourseProgress;
import com.example.study.databinding.FragmentTestsBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TestsFragment extends Fragment {

    private static final String ARG_COURSE_TITLE = "course_title";
    private FragmentTestsBinding binding;
    private String courseTitle;
    private int userId;
    private TestAdapter adapter;
    private ExecutorService executorService;
    private AppDatabase db;

    public static TestsFragment newInstance(String courseTitle) {
        TestsFragment fragment = new TestsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_COURSE_TITLE, courseTitle);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentTestsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * Настраивает RecyclerView, устанавливая LayoutManager и адаптер.
     */
    private void setupRecyclerView() {
        RecyclerView recyclerView = binding.rvTests;
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Получаем флаг нового аккаунта из SharedPreferences
        SharedPreferences userPrefs = requireContext().getSharedPreferences("ActiveUser", Context.MODE_PRIVATE);
        boolean isNewAccount = userPrefs.getBoolean("isNewAccount", false);
        // Сбрасываем флаг, чтобы при последующих заходах не очищались ответы
        userPrefs.edit().putBoolean("isNewAccount", false).apply();

        adapter = new TestAdapter(requireContext(), userId, new ArrayList<>(), this::updateProgress, isNewAccount);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        executorService = Executors.newSingleThreadExecutor();
        db = AppDatabase.getInstance(requireContext());

        SharedPreferences userPrefs = requireContext().getSharedPreferences("ActiveUser", Context.MODE_PRIVATE);
        userId = userPrefs.getInt("userId", -1);
        if (userId == -1) {
            // Если идентификатор пользователя не найден, возвращаемся назад
            getParentFragmentManager().popBackStack();
            return;
        }

        if (getArguments() != null) {
            courseTitle = getArguments().getString(ARG_COURSE_TITLE);
        }

        // Настраиваем RecyclerView
        setupRecyclerView();

        // Загружаем тесты для данного курса
        List<Test> tests = loadTests(courseTitle);
        adapter.setTests(tests);
    }

    /**
     * Обновляет прогресс курса в базе данных и уведомляет MaterialActivity.
     */
    private void updateProgress(int correctCount, int totalTests) {
        int progress = (correctCount * 100) / totalTests;

        executorService.execute(() -> {
            CourseProgress cp = new CourseProgress(userId, courseTitle, progress);
            db.courseProgressDao().insertOrUpdate(cp);
        });

        MaterialActivity activity = (MaterialActivity) getActivity();
        if (activity != null) {
            activity.updateCourseProgress(courseTitle, progress);
        }
    }

    /**
     * Загружает список тестов для указанного курса.
     */
    private List<Test> loadTests(String courseTitle) {
        List<Test> tests = new ArrayList<>();
        switch (courseTitle) {
            case "Английский язык":
                tests.add(new Test("Какое время используется для постоянных действий?",
                        new String[]{"Past Simple", "Present Simple", "Future Simple"}, 1));
                tests.add(new Test("Какой артикль используется перед словом 'apple'?",
                        new String[]{"a", "an", "the"}, 1));
                tests.add(new Test("Какое местоимение используется для первого лица единственного числа?",
                        new String[]{"I", "You", "He"}, 0));
                tests.add(new Test("Как правильно образуется множественное число существительных, оканчивающихся на 'y'?",
                        new String[]{"-ies", "-s", "-es"}, 0));
                tests.add(new Test("Выберите правильную форму глагола: 'She ___ to school every day.'",
                        new String[]{"go", "goes", "gone"}, 1));
                tests.add(new Test("Что означает фразовый глагол 'look up'?",
                        new String[]{"Смотреть вверх", "Искать информацию", "Ухаживать за кем-то"}, 1));
                tests.add(new Test("Какое из следующих слов является наречием?",
                        new String[]{"Quick", "Quickly", "Quickness"}, 1));
                tests.add(new Test("Заполните пропуск: 'They have been friends ___ five years.'",
                        new String[]{"since", "for", "ago"}, 1));
                tests.add(new Test("Какой предлог используется после глагола 'interested'?",
                        new String[]{"in", "at", "on"}, 0));
                break;
            case "Немецкий язык":
                tests.add(new Test("Какой артикль у слова 'Haus'?",
                        new String[]{"der", "die", "das"}, 2));
                tests.add(new Test("Переведите слово 'учить'.",
                        new String[]{"lernen", "arbeiten", "spielen"}, 0));
                tests.add(new Test("Какое слово означает 'книга' на немецком?",
                        new String[]{"Buch", "Stuhl", "Tisch"}, 0));
                tests.add(new Test("Выберите правильный перевод: 'Я люблю музыку'.",
                        new String[]{"Ich liebe Musik", "Ich liebe Bücher", "Ich liebe Essen"}, 0));
                tests.add(new Test("Какой глагол является неправильным?",
                        new String[]{"sein", "haben", "machen"}, 2));
                tests.add(new Test("Какое слово означает 'машина' на немецком?",
                        new String[]{"Auto", "Fahrrad", "Bus"}, 0));
                tests.add(new Test("Выберите правильный перевод: 'Мы идем в кино'.",
                        new String[]{"Wir gehen ins Kino", "Wir gehen in das Kino", "Wir gehen zu Kino"}, 0));
                tests.add(new Test("Какой артикль используется перед словом 'Blume'?",
                        new String[]{"der", "die", "das"}, 1));
                tests.add(new Test("Переведите фразу: 'Она читает книгу'.",
                        new String[]{"Sie liest ein Buch", "Sie liest Bücher", "Sie liest das Buch"}, 0));
                tests.add(new Test("Какой предлог используется с глаголом 'warten'?",
                        new String[]{"auf", "an", "über"}, 0));
                break;
            case "Программирование":
                tests.add(new Test("Что такое переменная?",
                        new String[]{"Алгоритм", "Контейнер для хранения данных", "Функция"}, 1));
                tests.add(new Test("Какая из этих структур данных линейная?",
                        new String[]{"Стек", "Дерево", "Граф"}, 0));
                tests.add(new Test("Что такое цикл 'for'?",
                        new String[]{"Условный оператор", "Циклический оператор", "Функция"}, 1));
                tests.add(new Test("Какой из следующих языков является объектно-ориентированным?",
                        new String[]{"C", "Java", "HTML"}, 1));
                tests.add(new Test("Что такое рекурсия?",
                        new String[]{"Повторение кода", "Вызов функции самой себя", "Использование циклов"}, 1));
                tests.add(new Test("Что делает метод 'toString()' в Java?",
                        new String[]{"Преобразует объект в строку", "Конвертирует строку в объект", "Удаляет объект"}, 0));
                tests.add(new Test("Какой модификатор доступа позволяет доступ только внутри класса?",
                        new String[]{"public", "private", "protected"}, 1));
                tests.add(new Test("Что такое наследование в ООП?",
                        new String[]{"Создание экземпляра класса", "Наследование свойств и методов от другого класса", "Переопределение методов"}, 1));
                tests.add(new Test("Какой из следующих методов используется для сортировки массива в Java?",
                        new String[]{"Arrays.sort()", "Collections.sort()", "List.sort()"}, 0));
                tests.add(new Test("Что такое интерфейс в Java?",
                        new String[]{"Класс с реализацией методов", "Тип данных", "Контракт, который класс должен реализовать"}, 2));
                break;
            case "Французский язык":
                tests.add(new Test("Какое слово означает 'собака'?",
                        new String[]{"chat", "chien", "oiseau"}, 1));
                tests.add(new Test("Какой артикль используется перед словом 'homme'?",
                        new String[]{"un", "une", "le"}, 0));
                tests.add(new Test("Как перевести 'яблоко' на французский?",
                        new String[]{"pomme", "banane", "orange"}, 0));
                tests.add(new Test("Выберите правильный перевод: 'Я живу в Париже'.",
                        new String[]{"Je vis à Paris", "Je vis à Lyon", "Je vis à Marseille"}, 0));
                tests.add(new Test("Какое местоимение используется для третьего лица женского рода?",
                        new String[]{"Il", "Elle", "Ils"}, 1));
                tests.add(new Test("Какое слово означает 'кофе' на французском?",
                        new String[]{"Thé", "Café", "Jus"}, 1));
                tests.add(new Test("Выберите правильный перевод: 'Он работает инженером'.",
                        new String[]{"Il travaille comme ingénieur", "Il travaille ingénieur", "Il est travail ingénieur"}, 0));
                tests.add(new Test("Какой артикль используется перед словом 'fille'?",
                        new String[]{"le", "la", "les"}, 1));
                tests.add(new Test("Переведите фразу: 'Мы говорим по-французски'.",
                        new String[]{"Nous parlons français", "Nous parlons en français", "Nous parlons de français"}, 0));
                tests.add(new Test("Какой предлог используется с глаголом 'aimer'?",
                        new String[]{"à", "de", "pas"}, 0));
                break;
            case "Математика":
                tests.add(new Test("Чему равно 2+2?",
                        new String[]{"3", "4", "5"}, 1));
                tests.add(new Test("Чему равно 5×3?",
                        new String[]{"15", "25", "10"}, 0));
                tests.add(new Test("Чему равно 12÷4?",
                        new String[]{"2", "3", "4"}, 1));
                tests.add(new Test("Какое число является простым?",
                        new String[]{"4", "6", "7"}, 2));
                tests.add(new Test("Решите уравнение: 3x - 5 = 10. Чему равно x?",
                        new String[]{"5", "15", "3"}, 0));
                tests.add(new Test("Чему равно √81?",
                        new String[]{"7", "8", "9"}, 2));
                tests.add(new Test("Какое из следующих чисел является квадратом?",
                        new String[]{"15", "16", "17"}, 1));
                tests.add(new Test("Найдите периметр прямоугольника со сторонами 5 и 8.",
                        new String[]{"13", "26", "40"}, 1));
                tests.add(new Test("Чему равно 7³?",
                        new String[]{"343", "49", "21"}, 0));
                tests.add(new Test("Какое свойство удовлетворяет равенство a + b = b + a?",
                        new String[]{"Ассоциативность", "Коммутативность", "Дистрибутивность"}, 1));
                break;
            default:
                // Если курс не распознан, можно вернуть пустой список или задать тесты по умолчанию.
                break;
        }
        return tests;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
        executorService.shutdown();
    }
}
