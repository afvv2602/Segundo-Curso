from django import forms
from .models import Author

# Se crea un formulario con todos los campos del modelo author
class AuthorForm(forms.ModelForm):
    class Meta:
        model = Author
        fields = ['first_name', 'last_name', 'date_of_birth', 'date_of_death']
