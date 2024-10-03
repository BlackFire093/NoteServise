package ru.netology

//класс описывающий заметку
data class Note(val id: Int, val title: String, val text: String, val comments: MutableList<Comment>)
//класс комментария к заметке
data class Comment(val id: Int, val message: String)
//класс удаленного комментария
data class DeleteComment(val note_id: Int, val comment_id: Int, val message: String)
class PostNotFoundException(message: String): RuntimeException(message)

fun main() {
    //создаем заметки
    WallService.add("Заметка 1", "Какой прекрасный день!")
    WallService.add("Заметка 2", "Сегодня в школе был день знаний.")
    WallService.add("Заметка 3", "Рядом с моим домом прекрасный парк.")
    println("Все заметки:")
    WallService.print(WallService.notes)//вывод заметок

    //изменяет заметку
    WallService.edit(1, "Заметка 1 изменена", "Замечательный день!!!")
    println("Заметки после изменения:")
    WallService.print(WallService.notes)//вывод заметок

    //создаем комментарии к заметкам
    WallService.createComment(1, "Отлично")
    WallService.createComment(1, "Супер!!!")
    WallService.createComment(2, "День знаний всегда хорошо.")
    WallService.createComment(2, "Я очень рад")
    WallService.createComment(3, "Это замечательно!!!")
    WallService.createComment(3, "Прекрасно!!!")
    println("Заметки с комментариями:")
    WallService.print(WallService.notes)//вывод заметок

    //изменяем комментарий у заметки
    WallService.editComment(1, 2, "Все просто супер!!!!!")
    println("Заметки после изменения комментария:")
    WallService.print(WallService.notes)//вывод заметок

    //получение заметок по кодам
    println("Заметки по кодам")
    WallService.print(WallService.get(mutableListOf(2, 3)))
    //получение заметки по id
    println("Заметка по id")
    println(WallService.getById(1))
    println()

    //получение комментариев у выбранной заметки
    println("Комментарии у заметки")
    println(WallService.getComments(1))
    println()

    //удаляем заметку
    WallService.delete(1)
    println("Заметки после удаления:")
    WallService.print(WallService.notes)//вывод заметок

    //удаляем комментарий к заметке
    WallService.deleteComment(2, 1)
    println("Заметки после удаления комментария:")
    WallService.print(WallService.notes)//вывод заметок

    //восстанавливаем комментарий
    WallService.restoreComment(2, 1)
    println("Заметки после восстановления комментария:")
    WallService.print(WallService.notes)//вывод заметок
}

object WallService {
    var notes = mutableListOf<Note>()//коллекция заметок
    var deleteComments = mutableListOf<DeleteComment>()
    private var lastId = 0

    //создание заметки
    fun add(title: String, text: String) {
        val note = Note(0, title, text, mutableListOf())
        notes += note.copy(id = ++lastId)
    }

    //добавление комментария к заметке
    fun createComment(note_id: Int, message: String): Boolean {
        for ((index, note) in notes.withIndex())
            if (note.id == note_id) {
                var id_comment = 0
                //формируем id комментария с учетом уже существующих
                do {
                    id_comment++
                } while (checkIdComment(note.id, id_comment, note.comments))
                notes[index].comments += Comment(id_comment, message)
                return true
            }
        return false
    }

    //проверка на существование id комментария, если true, то id существует
    fun checkIdComment(note_id: Int, comment_id: Int, comments: MutableList<Comment>): Boolean {
        var result = false
        //проверка по id существующих комментариев
        for (comment in comments)
            if (comment_id == comment.id) return true
        //проверка по id удаленных комментариев
        for (comment in deleteComments)
            if (note_id == comment.note_id && comment_id == comment.comment_id) return true
        return false
    }

    //удаление заметки
    fun delete(note_id: Int): Boolean {
        for ((index, note) in notes.withIndex())
            if (note_id == note.id) {
                //удаляем комментарии заметки, которые в удаленном списке, если есть
                var i = 0
                while (i < deleteComments.size) {
                    var comment = deleteComments[i]
                    if (comment.note_id == note_id) {
                        deleteComments.removeAt(i)
                        i--
                    }
                }
                //удаляем заметку
                notes.remove(note)
                return true
            }
        return false
    }

    //удаление заметки
    fun deleteComment(note_id: Int, comment_id: Int): Boolean {
        for ((index, note) in notes.withIndex())
            if (note_id == note.id) {
                var comments = note.comments
                for (comment in comments)
                    if (comment_id == comment.id) {
                        var del_comment = DeleteComment(note_id, comment_id, comment.message)
                        deleteComments.add(del_comment)
                        comments.remove(comment)
                        return true
                    }
                return false
            }
        return false
    }

    //изменение заметки
    fun edit(note_id: Int, title: String, text: String): Boolean {
        for ((index, note) in notes.withIndex())
            if (note_id == note.id) {
                var new_note = Note(note_id, title, text, note.comments)
                notes[index] = new_note
                return true
            }
        return false
    }

    //изменение комментария у заметки
    fun editComment(note_id: Int, comment_id: Int, message: String): Boolean {
        for (note in notes)
            if (note_id == note.id) {
                var comments = note.comments
                for ((index, comment) in comments.withIndex())
                    if (comment_id == comment.id) {
                        var new_comment = Comment(comment_id, message)
                        comments[index] = new_comment
                        return true
                    }
                return false
            }
        return false
    }

    //получение заметок по кодам
    fun get(note_ids: MutableList<Int>): MutableList<Note> {
        var result = mutableListOf<Note>()
        for (note in notes) {
            for (i in note_ids)
                if (note.id == i) {
                    result.add(note)
                    break
                }
        }
        return result
    }

    //получение заметки по id
    fun getById(note_id: Int): Note? {
        for (note in notes)
            if (note.id == note_id) return note
        return null
    }

    //возвращение комментариев по id заметки
    fun getComments(note_id: Int): MutableList<Comment>? {
        for (note in notes)
            if (note.id == note_id) return note.comments
        return null
    }

    //восстановление удаленного комментария
    fun restoreComment(note_id: Int, comment_id: Int): Boolean {
        for (comment in deleteComments)
            if (comment.note_id == note_id && comment.comment_id == comment_id) {
                var new_comment = Comment(comment.comment_id, comment.message)
                for (note in notes)
                    if (note.id == note_id) {
                        note.comments.add(new_comment)
                        return true
                    }
                return false
            }
        return false
    }

    //вывод, используется обобщение
    fun <T> print(list: MutableList<T>) {
        for (obj in list) {
            print(obj)
            println()
        }
        println()
    }
}