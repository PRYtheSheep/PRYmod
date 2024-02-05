package net.mod.prymod.Renderer;

public class RGB {
    public float r;
    public float g;
    public float b;

    public RGB(float r, float g, float b){
        this.r = r;
        this.g = g;
        this.b = b;
    }

    public boolean equals(RGB rgb) {return this.r == rgb.r && this.g == rgb.g && this.b == rgb.b;}
}
