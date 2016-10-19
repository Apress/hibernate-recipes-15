angular.module('authorApp', ['ui.router', 'ngRoute', 'ngResource', 'authorApp.controllers', 'authorApp.services']);

angular.module('authorApp').config(function($stateProvider) {
    $stateProvider.state('authors', {
        url: '/authors',
        templateUrl: 'partials/authors.html',
        controller: 'AuthorListController'
    }).state('viewAuthor', {
        url: '/authors/:id',
        templateUrl: 'partials/author-view.html',
        controller: 'AuthorViewController'
    }).state('newAuthor', {
        url: '/authors/new',
        templateUrl: 'partials/author-add.html',
        controller: 'AuthorCreateController'
    }).state('editAuthor', {
        url: '/authors/:id/edit',
        templateUrl: 'partials/author-edit.html',
        controller: 'AuthorEditController'
    }).state('newBook', {
        url: '/authors/:id/books/new',
        templateUrl: 'partials/book-add.html',
        controller: 'BookCreateController'
    }).state('books', {
        url: '/books',
        templateUrl: 'partials/authors.html',
        controller: 'AuthorListController'
    }).state('editBook', {
        url: '/authors/:id/books/:bid/edit',
        templateUrl: 'partials/book-edit.html',
        controller: 'BookEditController'
    });
}).run(function($state) {
    $state.go('authors');
});