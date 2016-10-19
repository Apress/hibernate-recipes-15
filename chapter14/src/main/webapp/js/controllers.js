angular.module('authorApp.controllers', [])

    .controller('AuthorListController', function ($scope, $state, popupService, $window, dataFactory) {
        dataFactory.getAuthors().success(function (authors) {
            $scope.authors = authors;
        }).error(function (error) {
            $scope.status = 'Unable to load author data: ' + error.message;
        });

        $scope.deleteAuthor = function (author) {
            if (popupService.showPopup('Really delete this?')) {
                dataFactory.deleteAuthor(author.id).success(function () {
                    $window.location.href = '';
                });
            }
        };

    }).controller('AuthorViewController', function ($scope, $stateParams, popupService, AuthorService, dataFactory) {

        console.log("Getting books for sp: " + $stateParams.id);
        if(typeof $stateParams.id === 'undefined'){
            id = AuthorService.getAuthorId();
        } else {
            id = $stateParams.id;
        }
        dataFactory.getAuthor(id).success(function (author) {
            $scope.author = author;

            console.log("Getting books for: " + author.id);
            dataFactory.getBooks(author.id).success(function (books) {
                console.log("Books: " + books.length);
                $scope.books = books;
            })
        });

        $scope.deleteBook = function (author, book) {
            if (popupService.showPopup('Really delete this book?')) {
                console.log("Book: " + book.id + " Author: " + author.id);
                dataFactory.deleteBook(author.id, book.id).success(function () {
                    $window.location.href = '';
                });
            }
        };

    }).controller('AuthorCreateController', function ($scope, $state, $stateParams, dataFactory) {
        $scope.author = {};

        $scope.addAuthor = function () {
            dataFactory.insertAuthor($scope.author).success(function () {
                $state.go('authors');
            });
        }
    }).controller('AuthorEditController', function ($scope, $state, $stateParams, Author, dataFactory) {

        $scope.updateAuthor = function () {
            dataFactory.updateAuthor($scope.author).success(function () {
                $state.go('authors'); // on success go back to home i.e. authors state.
            });
        };

        $scope.loadAuthor = function () {
            dataFactory.getAuthor($stateParams.id).success(function (author) {
                $scope.author = author;
            })
        };

        $scope.loadAuthor();
    }).controller('BookEditController', function ($scope, $state, $stateParams, AuthorService, dataFactory) {

        $scope.updateBook = function () {
            console.log("Author: " + $stateParams.id);
            console.log("Book: " + $scope.book);
            dataFactory.updateBook($stateParams.id, $scope.book).success(function () {
                AuthorService.setAuthorId($stateParams.id);
                $state.go('viewAuthor');
            }).error(function (error) {
                $scope.status = 'Error creating book! ' + error;
                console.log($scope.status);
                console.log(error);
            });
        };

        $scope.loadBook = function () {
            console.log($stateParams.id);
            dataFactory.getBook($stateParams.id, $stateParams.bid).success(function (book) {
                $scope.book = book;
                console.log(book);
            });
            dataFactory.getAuthor($stateParams.id).success(function (author) {
                $scope.author = author;
            });
        };

        $scope.loadBook();
    }).controller('BookCreateController', function ($scope, $state, $stateParams, dataFactory) {
        console.log("Creating book");
        $scope.book = {};

        dataFactory.getAuthor($stateParams.id).success(function (author) {
            $scope.book.author = author;

            $scope.addBook = function () {
                console.log($stateParams.id);
                dataFactory.insertBook(author.id, $scope.book).success(function () {
                    $state.go('viewAuthor');
                }).error(function (error) {
                    $scope.status = 'Error creating book! ' + error;
                    console.log($scope.status);
                    console.log(error);
                });
            }
        })
    });