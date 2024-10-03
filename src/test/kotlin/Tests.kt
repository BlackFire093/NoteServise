import ru.netology.*
import org.junit.Assert.*
import org.junit.Test

class Tests {
    //создаем заметки с комментариями
    fun CreateNotes(){
        //создаем заметки
        WallService.add("Заметка 1", "Какой прекрасный день!")
        WallService.add("Заметка 2", "Сегодня в школе был день знаний.")
        WallService.add("Заметка 3", "Рядом с моим домом прекрасный парк.")
        //создаем комментарии к заметкам
        WallService.createComment(1, "Отлично")
        WallService.createComment(1, "Супер!!!")
        WallService.createComment(2, "День знаний всегда хорошо.")
        WallService.createComment(2, "Я очень рад")
        WallService.createComment(3, "Это замечательно!!!")
        WallService.createComment(3, "Прекрасно!!!")
    }
    //тест на добавление комментария
    @Test
    fun TestAddComment(){
        CreateNotes()
        assertEquals(false, WallService.createComment(1, "Ура!!!"))
    }
    //тест на удаление заметки
    @Test
    fun TestDeleteNote(){
        CreateNotes()
        assertEquals(true, WallService.delete(1))
    }
    //тест на изменение заметки
    @Test
    fun TestEditNote(){
        CreateNotes()
        assertEquals(true, WallService.edit(1, "Заметка", "Изменено"))
    }
    //тест на изменение комментария у заметки
    @Test
    fun TestWditComment(){
        CreateNotes()
        assertEquals(true, WallService.editComment(1, 2, "Изменено"))
    }
    //тест на получение заметки по id
    @Test(expected = PostNotFoundException::class)
    fun TestGetNote(){
        CreateNotes()
        WallService.getById(100) ?: throw PostNotFoundException("ERROR")
    }
    //тест на возвращения комментария по кодам заметки и комментария
    @Test(expected = PostNotFoundException::class)
    fun TestGetComment(){
        CreateNotes()
        WallService.getComments(100) ?: throw PostNotFoundException("ERROR")
    }
    //тест на восстановление удаленного комментария
    @Test
    fun TestRestoreComment(){
        CreateNotes()
        WallService.deleteComment(1, 1)
        assertEquals(false, WallService.restoreComment(1, 1))
    }
}