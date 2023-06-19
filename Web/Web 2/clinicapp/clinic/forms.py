from django import forms
from .models import Cita

class NuevaCitaForm(forms.ModelForm):
    class Meta:
        model = Cita
        fields = ['fecha', 'hora', 'medico']
        widgets = {
            'fecha': forms.DateInput(attrs={'placeholder': 'yyyy-mm-dd'}),
            'hora': forms.TimeInput(attrs={'placeholder': 'HH:MM'}),
        }
