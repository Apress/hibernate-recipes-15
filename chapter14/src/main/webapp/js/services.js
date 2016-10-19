angular.module('authorApp.services', [])

    .factory('Author', function ($resource) {
        return $resource('/app/library/authors/:id', {id: '@id'}, {
            update: {
                method: 'PUT'
            }
        });

    }).factory('Books', function ($resource) {
        return $resource('/app/library/authors/:authorId/books/:bookId',
            {authorId: '@authorId', bookId: ' @bookId'}, {
                update: {
                    method: 'PUT'
                }
            })

    }).service('popupService', function ($window) {
        this.showPopup = function (message) {
            return $window.confirm(message);
        }
    }).factory('authorService', ['_', function(_){
        function AuthorService(){
            this.author.id = 0;
            this.book.id = 0;
        }

        AuthorService.prototype.setAuthorId = function(id){
            this.author.id = id;
        };

        AuthorService.prototype.getAuthorId = function(){
            return this.author.id;
        };

        return new AuthorService();
    }]).factory('dataFactory', ['$http', function ($http) {

        var urlBase = '/app/library/authors';
        var dataFactory = {};

        dataFactory.getAuthors = function () {
            return $http.get(urlBase);
        };

        dataFactory.getAuthor = function (id) {
            return $http.get(urlBase + '/' + id);
        };

        dataFactory.insertAuthor = function (author) {
            return $http.post(urlBase, author);
        };

        dataFactory.updateAuthor = function (author) {
            return $http.put(urlBase + '/' + author.id, author)
        };

        dataFactory.deleteAuthor = function (id) {
            return $http.delete(urlBase + '/' + id);
        };

        dataFactory.getBooks = function (id) {
            return $http.get(urlBase + '/' + id + '/books');
        };

        dataFactory.getBook = function(authorId, bookId) {
            return $http.get(urlBase + '/' + authorId + '/books/' +  bookId);
        };

        dataFactory.insertBook = function(authorId, book) {
            return $http.post(urlBase + '/' + authorId + '/books', book);
        };

        dataFactory.updateBook = function(authorId, book) {
            return $http.put(urlBase + '/' + authorId + '/books/' + book.id, book);
        };

        dataFactory.deleteBook = function(authorId, bookId) {
            return $http.delete(urlBase + '/' + authorId + '/books/' + bookId);
        };


        return dataFactory;
    }]);

