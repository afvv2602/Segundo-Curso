from django.db import models
from django.contrib.auth.models import User
from django.urls import reverse

class Perfil(models.Model):
    TIPO_USUARIO_CHOICES = [
        ('PAC', 'Paciente'),
        ('DOC', 'Doctor'),
    ]
    user = models.OneToOneField(User, on_delete=models.CASCADE, related_name='perfil')
    tipo_usuario = models.CharField(max_length=3, choices=TIPO_USUARIO_CHOICES, default='PAC')

    def __str__(self):
        return self.user.username

class Pregunta(models.Model):
    titulo = models.CharField(max_length=200)
    descripcion = models.TextField()
    perfil = models.ForeignKey(Perfil, related_name='preguntas', on_delete=models.CASCADE)
    medico = models.ForeignKey(Perfil, related_name='respuestas', on_delete=models.SET_NULL, null=True, blank=True)
    respuesta = models.TextField(blank=True)

class Cita(models.Model):
    fecha = models.DateField()
    hora = models.TimeField()
    medico = models.ForeignKey(Perfil, related_name='citas_doctor', on_delete=models.CASCADE)
    paciente = models.ForeignKey(Perfil, related_name='citas_paciente', on_delete=models.CASCADE)
    cancelada = models.BooleanField(default=False)
