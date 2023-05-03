from django.shortcuts import render, get_object_or_404, redirect
from .models import Book, Author, BookInstance, Genre
from django.views import generic
from .forms import AuthorForm
from django.urls import reverse
from django.utils.html import format_html


def index(request):
    # Vista para la página principal del sitio.
    num_books = Book.objects.all().count()
    num_instances = BookInstance.objects.all().count()
    num_instances_available = BookInstance.objects.filter(status__exact='a').count()
    num_authors = Author.objects.count()

    context = {
        'num_books': num_books,
        'num_instances': num_instances,
        'num_instances_available': num_instances_available,
        'num_authors': num_authors,
    }

    return render(request, 'index.html', context=context)

def new_author(request):
    # Vista para añadir un nuevo autor a la base de datos.
    if request.method == 'POST':
        form = AuthorForm(request.POST)
        if form.is_valid():
            author = form.save()
            return redirect('author-detail', pk=author.pk)
    else:
        form = AuthorForm()
    return render(request, 'catalog/author_form.html', {'form': form})

class BookListView(generic.ListView):
    # Vista para mostrar la lista de libros.
    model = Book

    def get_context_data(self, **kwargs):
        context = super(BookListView, self).get_context_data(**kwargs)
        context['some_data'] = 'This is just some data'
        return context

class BookDetailView(generic.DetailView):
    # Vista para mostrar los detalles de un libro.
    model = Book

    def book_detail_view(request, primary_key):
        book = get_object_or_404(Book, pk=primary_key)
        return render(request, 'catalog/book_detail.html', context={'book': book})

class AuthorListView(generic.ListView):
    # Vista para mostrar la lista de autores.
    model = Author

    def get_context_data(self, **kwargs):
        context = super(AuthorListView, self).get_context_data(**kwargs)
        context['some_data'] = 'This is just some data'
        context['add_author_url'] = reverse('add-author')
        return context

    def add_author(request):
        # Vista para añadir un nuevo autor a la base de datos.
        if request.method == 'POST':
            form = AuthorForm(request.POST)
            if form.is_valid():
                form.save()
                return redirect('authors')
        else:
            form = AuthorForm()
        return render(request, 'catalog/author_form.html', {'form': form})

    def author_detail_view(request, primary_key):
        author = get_object_or_404(Author, pk=primary_key)
        return render(request, 'catalog/author_detail.html', context={'author': author})

class AuthorDetailView(generic.DetailView):
    # Vista para mostrar los detalles de un autor.
    model = Author

    def author_detail_view(request, primary_key):
        author = get_object_or_404(Author, pk=primary_key)
        return render(request, 'catalog/author_detail.html', context={'author': author})