package controllers

import ie.setu.models.Author

class API {
    private var authors = ArrayList<Author>()

    fun addAuthor(author: Author): Boolean {
        return authors.add(author)
    }
    fun listAllAuthors(): String {
        var listOfAuthors= ""
         if (authors.isEmpty()) {
            return "No authors yet"
        } else {
            for (i in authors.indices) {
                listOfAuthors+= "${i}: ${authors[i]} \n"
            }
        }
        return listOfAuthors
    }
   fun addingBookWithExistingAuthor (Id:Int) {


   }

}