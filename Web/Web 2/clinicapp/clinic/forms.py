from django import forms
from .models import Cita

# Definimos un formulario para crear nuevas citas
class NuevaCitaForm(forms.ModelForm):
    fecha = forms.DateField(
        widget=forms.DateInput(attrs={'type': 'date'}),
        help_text="Por favor, introduce la fecha en formato: yyyy-mm-dd"
    )
    hora = forms.TimeField(
        widget=forms.TimeInput(attrs={'type': 'time'}),
        help_text="Por favor, introduce la hora en formato: HH:MM"
    )
    class Meta:
        model = Cita
        fields = ['fecha', 'hora', 'medico']
