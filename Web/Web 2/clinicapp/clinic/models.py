from django.db import models
from django.contrib.auth.models import User

class Perfil(models.Model):
    TIPO_USUARIO_CHOICES = [
        ('PAC', 'Paciente'),
        ('DOC', 'Doctor'),
    ]
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    tipo_usuario = models.CharField(max_length=3, choices=TIPO_USUARIO_CHOICES)

class Pregunta(models.Model):
    titulo = models.CharField(max_length=200)
    descripcion = models.TextField()
    paciente = models.ForeignKey(User, related_name='preguntas', on_delete=models.CASCADE)
    medico = models.ForeignKey(User, related_name='respuestas', on_delete=models.SET_NULL, null=True)
    respuesta = models.TextField(blank=True)

class Cita(models.Model):
    ESPECIALIDAD_CHOICES = [
        # Aquí debes agregar las especialidades que necesites
        ('GEN', 'General'),
        ('PED', 'Pediatria'),
        # etc.
    ]
    fecha = models.DateField()
    hora = models.TimeField()
    especialidad = models.CharField(max_length=3, choices=ESPECIALIDAD_CHOICES)
    medico = models.ForeignKey(User, related_name='citas_doctor', on_delete=models.CASCADE)
    paciente = models.ForeignKey(User, related_name='citas_paciente', on_delete=models.CASCADE)
